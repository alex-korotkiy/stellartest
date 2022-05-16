package utils

import org.mongodb.scala.model.Filters
import org.mongodb.scala.{Document, MongoClient, MongoCollection, MongoDatabase}
import org.mongodb.scala.bson.collection._
import org.mongodb.scala.result.{DeleteResult, InsertOneResult}

import scala.concurrent.Await
import scala.concurrent.duration.Duration

object MongoUtils {

  val mongoClient: MongoClient = MongoClient("mongodb://localhost:27017/")
  val database: MongoDatabase = mongoClient.getDatabase("trandb")

  def deleteDocument(collection: MongoCollection[Document], key: Long): DeleteResult = {
    val filter = Filters.eq("_id", key)
    Await.result(collection.deleteOne(filter).toFuture(), Duration.Inf)
  }

  def deleteDocument(collectionName: String, key: Long):  DeleteResult = {
    val collection: MongoCollection[Document] = database.getCollection[Document](collectionName)
    deleteDocument(collection, key)
  }

  def upsertDocument(collectionName: String, document: Document, key: Long): InsertOneResult = {
    val collection: MongoCollection[Document] = database.getCollection[Document](collectionName)
    deleteDocument(collection, key)
    val newDoc = mutable.Document(document) ++ Document("_id" -> key)
    Await.result(collection.insertOne(newDoc.toBsonDocument()).toFuture(), Duration.Inf)
  }

/*
  def upsertObject[T, U](collectionName: String, instance: T,  key: T => U)(implicit encoder: Encoder[T]) = {
    val collection = database.getCollection(collectionName)
    val filter = Filters.eq("_id", key(instance))
    val bson = Document(instance.asJson.toString())
    val options = new UpdateOptions()
    options.upsert(true)
    val future = collection.updateOne(filter, bson, options).toFuture()
    Await.result(future, Duration.Inf)
  }

 */
}
