package messages


import akka.actor.typed.ActorRef
import models.stellartest.{Record, TransactionsQueryResult}

sealed trait TransactionsMessage
final case class TransactionsData(queryResult: TransactionsQueryResult) extends TransactionsMessage

sealed trait FetchMessage
final case class FetchTransactions(url: String, persister: ActorRef[TransactionsMessage]) extends FetchMessage
