import dto.Configuration
import io.circe.generic.auto.exportEncoder
import utils.MongoUtils
import io.circe.generic.auto._
import io.circe.syntax._
import org.mongodb.scala.bson.Document


object AppRunTest {

  def main(args: Array[String]): Unit = {

    val simpleDoc = Document("name" -> "me")
    MongoUtils.upsertDocument("names", simpleDoc, 1)

  }
}