package actors

import akka.actor.typed.{ActorRef, ActorSystem, Behavior}
import akka.actor.typed.scaladsl.Behaviors
import dto.{Backward, Direction, Forward}
import messages.{FetchMessage, FetchTransactions, TransactionsData}
import utils.StUtils.getTransactions

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.{Duration, FiniteDuration}
import scala.util.{Failure, Success, Try}

object Fetcher {

  def apply(direction: Direction, fetchDelayOnEmpty: FiniteDuration, retryDelay: FiniteDuration): Behavior[FetchMessage] = Behaviors.setup { ctx =>

    Behaviors.receiveMessage {
      case FetchTransactions(url, persister) =>
        ctx.log.info(s"Fetching transactions from $url")
        val fetchResult = getTransactions(url)
        fetchResult match {

          case Success(tranResult) =>
            ctx.log.info(s"Fetching transactions from $url success")
            persister ! TransactionsData(tranResult)
            val transactions = tranResult._embedded.records
            if(transactions.isEmpty){
              direction match {
                case Forward => ctx.system.scheduler.scheduleOnce(fetchDelayOnEmpty,
                  new Runnable {
                    def run: Unit = {
                      ctx.self ! FetchTransactions(tranResult._links.next.href, persister)
                    }
                  })
                case Backward => return Behaviors.stopped
              }
            }
            else {
              ctx.self ! FetchTransactions(tranResult._links.next.href, persister)
            }

          case Failure(exception) =>
            ctx.log.info(s"Fetching transactions from $url failed with exception: ${exception.getMessage}")
            ctx.system.scheduler.scheduleOnce(retryDelay,
              new Runnable {
                def run: Unit = {
                  ctx.self ! FetchTransactions(url, persister)
                }
              })
        }

        Behaviors.same
    }
  }
}
