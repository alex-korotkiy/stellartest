package utils

import scala.concurrent.duration.Duration
import scala.util.{Failure, Success, Try}

object Misc {
  def retry[T](action: () => T, retryCount: Int, delay: Duration): Try[T] = {
    val result = Try(action())
    if (retryCount <= 0) return result
    result match {
      case Success(_) => result
      case Failure(_) =>
        Thread.sleep(delay.toMillis)
        retry(action, retryCount-1, delay)
    }
  }
}
