package actors

import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors
import dto.Borders
import messages._
import models.stellartest.Record
import utils.Db

class TransactionsPersister(startLedger: Int = 0, endLedger: Int = Int.MaxValue, var borders: Option[Borders]) {
  def apply(): Behavior[TransactionsMessage] = Behaviors.setup { ctx =>

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
    records.filter(r=> r.ledger>=startLedger && r.ledger <= endLedger).foreach(Db.upsertRecord)
    val minToken = tokens.min
    val maxToken = tokens.max
    adjustBorders(Borders(minToken, maxToken))
  }

  def adjustBorders(interval: Borders): Unit = {
    borders match {
      case Some(bordersValue) =>
        val newLowBorder = List(bordersValue.low, interval.low).min
        val newHighBorder = List(bordersValue.high, interval.high).max
        if (newLowBorder < bordersValue.low) Db.updateLowBorder(newLowBorder)
        if (newHighBorder > bordersValue.high) Db.updateHighBorder(newHighBorder)
        borders = Some(Borders(newLowBorder, newHighBorder))
      case None =>
        Db.updateLowBorder(interval.low)
        Db.updateHighBorder(interval.high)
        borders = Some(interval)
    }
  }

}
