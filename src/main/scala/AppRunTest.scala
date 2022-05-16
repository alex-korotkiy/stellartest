import dto.{Borders, Configuration}
import io.circe.generic.auto.exportEncoder
import utils.{MongoUtils, Repository}
import io.circe.generic.auto._


object AppRunTest {

  def main(args: Array[String]): Unit = {
    Repository.saveLedgersRange(1, 3)
    var ledgers = Repository.getLedgersRange()
    println(ledgers)
  }
}