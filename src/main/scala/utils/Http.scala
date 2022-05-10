package utils

import scala.util.Try

object Http {
  def get(url: String): Try[String] = Try(requests.get(url).text())
}
