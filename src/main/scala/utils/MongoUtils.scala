package utils

import io.circe.Encoder
import org.mongodb.scala.bson.{BsonObjectId, Document}
import org.mongodb.scala.model.{Filters, UpdateOptions}
import org.mongodb.scala.{MongoClient, MongoDatabase}
import io.circe.generic.auto._
import io.circe.parser._
import io.circe.syntax.EncoderOps

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration

object MongoUtils {

  val mongoClient: MongoClient = MongoClient("mongodb://localhost:27017/")
  val database: MongoDatabase = mongoClient.getDatabase("trandb")

  def upsertObject[T, U](collectionName: String, instance: T,  key: T => U)(implicit encoder: Encoder[T]) = {
    val collection = database.getCollection(collectionName)
    val filter = Filters.eq("_id", key(instance))
    val bson = Document(instance.asJson.toString())
    val options = new UpdateOptions()
    options.upsert(true)
    val future = collection.updateOne(filter, bson, options).toFuture()
    Await.result(future, Duration.Inf)
  }
}
