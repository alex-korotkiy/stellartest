import scala.concurrent.Await
import scala.concurrent.duration.Duration
import scala.io.Source
import io.circe._
import io.circe.generic.auto._
import io.circe.parser._
import io.circe.syntax._
import models.stellartest.RootInterface
import utils.Http

object AppRun {
  def main(args: Array[String]): Unit = {

    val json = Http.get("https://horizon-testnet.stellar.org/transactions?limit=50&order=desc").get
    val result = decode[RootInterface](json)
    result match {
      case Left(error) => println(error)
      case Right(rootInterface: RootInterface) => {
        rootInterface._embedded.records.foreach(
          r => println(r.asJson)
        )
      }
    }
  }
}