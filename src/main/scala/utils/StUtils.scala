package utils


import io.circe.generic.auto._
import io.circe.parser._
import models.stellartest.TransactionsQueryResult

import scala.util.Try

object StUtils {

  def getTransactions(url: String): Try[TransactionsQueryResult] = {
    val json = Http.get(url).get
    decode[TransactionsQueryResult](json).toTry
  }
}
