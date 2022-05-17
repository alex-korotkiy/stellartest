package utils

import dto.{Borders, SingleValue}
import io.circe.generic.auto.{exportDecoder, exportEncoder}
import models.stellartest.Record

import Misc._

object Repository {

  private val borders = "borders"
  private val ledgers = "ledgers"
  private val records = "records"

  //val records = mutable.Map[String, Record]()

  def getBorders: Option[Borders] = {
    val bordersList = MongoUtils.getAllObjects[SingleValue[Long]](borders).map(kv => kv._2.value)
    if(bordersList.isEmpty)
      None
    else
      Some(Borders(bordersList.min, bordersList.max))
  }

  def getLedgersRange(): (Int, Int) = {
    val ledgersList = MongoUtils.getAllObjects[SingleValue[Int]](ledgers).map(kv => (kv._1, kv._2.value))
    val startLedgers = ledgersList.filter(kv => kv._1 == 0).map(kv => kv._2)
    val startLedger = firstOfDefault(startLedgers, 0)
    val endLedgers = ledgersList.filter(kv => kv._1 == 1).map(kv => kv._2)
    val endLedger = firstOfDefault(endLedgers, Int.MaxValue)
    (startLedger, endLedger)
  }

  def saveLedgersRange(start: Int, end: Int)= {
    MongoUtils.upsertObject(ledgers, SingleValue(start), 0)
    MongoUtils.upsertObject(ledgers, SingleValue(end), 1)
  }

  def upsertRecord(record: Record) = MongoUtils.upsertObject(records, record, record.paging_token)

  def updateLowBorder(value: Long): Unit = {
    MongoUtils.upsertObject(borders, SingleValue(value), 0)
  }

  def updateHighBorder(value: Long): Unit = {
    MongoUtils.upsertObject(borders, SingleValue(value), 1)
  }

  def clearBorders() = {
    MongoUtils.deleteAll(borders)
  }


}
