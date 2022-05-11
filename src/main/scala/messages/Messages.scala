package messages

import models.stellartest.Record

sealed trait TransactionsMessage
final case class TransactionsList(transactions: List[Record]) extends TransactionsMessage

