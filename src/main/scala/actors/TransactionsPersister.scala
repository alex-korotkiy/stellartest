package actors

import akka.NotUsed
import akka.actor.typed.scaladsl.{AbstractBehavior, ActorContext, Behaviors}
import akka.actor.typed._
import dto.{Borders, Configuration}
import messages._
import models.stellartest.Record
import utils.Repository

object TransactionsPersister {

  var startLedger: Int = 0
  var endLedger: Int = Int.MaxValue
  var borders: Option[Borders] = None

  def apply(configuration: Configuration, borders: Option[Borders]): Behavior[TransactionsMessage] = Behaviors.setup { ctx =>

    this.startLedger = configuration.startLedger
    this.endLedger = configuration.endLedger
    this.borders = borders

    Behaviors.receiveMessage {
      case TransactionsData(queryResult) =>
        ctx.log.info("Transactions persister received transactions data")
        saveTransactions(queryResult._embedded.records)
        Behaviors.same
    }
  }

  def saveTransactions(records: Seq[Record]): Unit = {
    val tokens = records.map(r => r.paging_token)
    if (tokens.isEmpty) return
    records.filter(r=> r.ledger >= startLedger && r.ledger <= endLedger).foreach(Repository.upsertRecord)
    val minToken = tokens.min
    val maxToken = tokens.max
    adjustBorders(Borders(minToken, maxToken))
  }

  def adjustBorders(interval: Borders): Unit = {
    borders match {
      case Some(bordersValue) =>
        val newLowBorder = List(bordersValue.low, interval.low).min
        val newHighBorder = List(bordersValue.high, interval.high).max
        if (newLowBorder < bordersValue.low) Repository.updateLowBorder(newLowBorder)
        if (newHighBorder > bordersValue.high) Repository.updateHighBorder(newHighBorder)
        borders = Some(Borders(newLowBorder, newHighBorder))
      case None =>
        Repository.updateLowBorder(interval.low)
        Repository.updateHighBorder(interval.high)
        borders = Some(interval)
    }
  }

}
