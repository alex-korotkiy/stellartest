package utils

import scala.collection.mutable
import models.stellartest.Record


object Db {

  var lowBorder: Option[Long] = None
  var highBorder: Option[Long] = None

  val records = mutable.Map[String, Record]()

  def upsertRecord(record: Record) = records.update(record.id, record)

  def updateLowBorder(value: Long): Unit = {
    lowBorder = Some(value)
  }

  def updateHighBorder(value: Long): Unit = {
    highBorder = Some(value)
  }


}
