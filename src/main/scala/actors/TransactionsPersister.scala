package actors

import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorRef, ActorSystem, Behavior}
import messages._

class TransactionsPersister {
  def apply(): Behavior[TransactionsMessage] = Behaviors.setup { ctx =>

    Behaviors.receiveMessage {
      case TransactionsList(records) =>
        ctx.log.info("")
        Behaviors.same
    }
  }
}
