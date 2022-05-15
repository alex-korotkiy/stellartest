package actors

import akka.NotUsed
import akka.actor.ActorContext
import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors
import dto.{Backward, Borders, Configuration, Forward}
import messages.FetchTransactions
import utils.Misc.retry
import utils.{Repository, StUtils}

import scala.util.{Failure, Success}

object Starter {

  def apply(configuration: Configuration): Behavior[NotUsed] =
    Behaviors.setup { ctx =>

      ctx.log.info("starting data")
      val persister = ctx.spawn(TransactionsPersister(configuration, None), "persister")

      def startFetchers(forwardUrl: String, backwardUrl: String) = {
        val forwardFetcher = ctx.spawn(Fetcher(Forward, configuration.fetcherForwardDelayOnEmpty, configuration.fetcherRetryDelayOnError), "forward_fetcher")
        forwardFetcher ! FetchTransactions(forwardUrl , persister)
        val backwardFetcher = ctx.spawn(Fetcher(Backward, configuration.fetcherForwardDelayOnEmpty, configuration.fetcherRetryDelayOnError), "backward_fetcher")
        backwardFetcher ! FetchTransactions(backwardUrl , persister)
      }

      val savedLedgers = Repository.getLedgersRange()
      val savedBorders = Repository.getBorders

      val doNarrow = savedLedgers._1 <= configuration.startLedger && savedLedgers._2 >= configuration.endLedger

      val borders = doNarrow match {
        case true => savedBorders
        case false =>
          Repository.clearBorders()
          None
      }

      borders match {
        case Some(bordersValue) =>
          val startForwardUrl = s"${configuration.transactionsUrl}?cursor=${bordersValue.high}&limit=${configuration.batchSize}&order=asc"
          val startBackwardUrl = s"${configuration.transactionsUrl}?cursor=${bordersValue.low}&limit=${configuration.batchSize}&order=desc"
          startFetchers(startForwardUrl, startBackwardUrl)
          Behaviors.same
        case None =>
          val initUrl = s"${configuration.transactionsUrl}?limit=${configuration.batchSize}&order=desc"
          val getInitTransactions = () => StUtils.getTransactions(initUrl).get
          val initTranResult = retry(getInitTransactions, configuration.initRetriesCount, configuration.initRetryDelayOnError)

          initTranResult match {
            case Failure(exception) => {
              ctx.log.error(s"Failed to get transactions from url ${initUrl}:")
              ctx.log.error(exception.getMessage)
              Behaviors.stopped
            }
            case Success(transactions) =>
              val forwardFetcher = ctx.spawn(Fetcher(Forward, configuration.fetcherForwardDelayOnEmpty, configuration.fetcherRetryDelayOnError), "forward_fetcher")
              forwardFetcher ! FetchTransactions(transactions._links.prev.href , persister)
              val backwardFetcher = ctx.spawn(Fetcher(Backward, configuration.fetcherForwardDelayOnEmpty, configuration.fetcherRetryDelayOnError), "backward_fetcher")
              backwardFetcher ! FetchTransactions(transactions._links.next.href , persister)
          }

          Behaviors.same
      }

    }
}
