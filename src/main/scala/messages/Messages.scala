package messages

import models.stellartest.{Record, TransactionsQueryResult}

sealed trait TransactionsMessage
final case class TransactionsData(queryResult: TransactionsQueryResult) extends TransactionsMessage


