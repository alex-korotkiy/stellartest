import dto.Configuration
import io.circe.generic.auto.exportEncoder
import utils.MongoUtils
import io.circe.generic.auto._, io.circe.syntax._

object AppRunTest {

  case class C1(a: Int, b: String)

  def main(args: Array[String]): Unit = {
    MongoUtils.upsertObject[C1, Int]("test", C1(1, "ab"), c => c.a)
  }
}