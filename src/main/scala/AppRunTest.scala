import dto.{Borders, Configuration}
import io.circe.generic.auto.exportEncoder
import utils.MongoUtils
import io.circe.generic.auto._


object AppRunTest {

  def main(args: Array[String]): Unit = {

    val b1 = Borders(1,2)
    val b2 = Borders(2,4)

    MongoUtils.upsertObject("brdrs", b1, 1)
    MongoUtils.upsertObject("brdrs", b2, 2)

    val res = MongoUtils.getAllObjects[Borders]("brdrs")

    println(res)
  }
}