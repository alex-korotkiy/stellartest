
import akka.actor.typed.ActorSystem
import akka.NotUsed
import actors._
import com.dimafeng.testcontainers.MongoDBContainer
import dto.Configuration
import org.testcontainers.utility.DockerImageName

object AppRun{

    def main(args: Array[String]): Unit = {
        val container: MongoDBContainer = MongoDBContainer(DockerImageName.parse("mongo:4.0.10"))
        container.start()
        println(container.containerIpAddress)
        val config = Configuration()
        val starter = Starter(config)
        implicit val system: ActorSystem[NotUsed] = ActorSystem(starter, "dataStarter")
    }
}
