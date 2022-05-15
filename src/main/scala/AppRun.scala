
import akka.actor.typed.ActorSystem
import akka.NotUsed
import actors._
import dto.Configuration

object AppRun{

    def main(args: Array[String]): Unit = {
        val config = Configuration()
        val starter = Starter(config)
        implicit val system: ActorSystem[NotUsed] = ActorSystem(starter, "dataStarter")
    }
}
