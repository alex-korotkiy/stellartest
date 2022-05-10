import scala.util.Try

object HttpUtils {
  def get(url: String): Try[String] = Try(requests.get(url).text())
}
