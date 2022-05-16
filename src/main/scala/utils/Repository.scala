package utils

import dto.Borders

import scala.collection.mutable
import models.stellartest.Record

object Repository {

  private val borders = "borders"
  private val ledgers = "ledgers"

  var lowBorder: Option[Long] = None
  var highBorder: Option[Long] = None

  var startLedgerId: Int = 0
  var endLedgerId: Int = Int.MaxValue

  val records = mutable.Map[String, Record]()

  def getBorders: Option[Borders] = {
    val bordersList = lowBorder.toList ++ highBorder.toList
    if(bordersList.isEmpty)
      None
    else
      Some(Borders(bordersList.min, bordersList.max))
  }

  def getLedgersRange(): (Int, Int) = (startLedgerId, endLedgerId)

  def saveLedgersRange(start: Int, end: Int)= {
    startLedgerId = start
    endLedgerId = end
  }

  def upsertRecord(record: Record) = records.update(record.id, record)

  def updateLowBorder(value: Long): Unit = {
    lowBorder = Some(value)
  }

  def updateHighBorder(value: Long): Unit = {
    highBorder = Some(value)
  }

  def clearBorders() = {
    highBorder = None
    lowBorder = None
  }


}
