package com.example.practice.modules

import javax.inject.Singleton
import reactivemongo.api.collections.bson.BSONCollection
import reactivemongo.api.{DefaultDB, MongoConnection, MongoDriver}

import scala.concurrent.{ExecutionContext, Future}

object MongoDBMudule {

  @Singleton
  def getCollection(): Future[BSONCollection] ={
    val mongoUri = "mongodb://localhost:27017/test?authMode=scram-sha1"
    import ExecutionContext.Implicits.global

    val driver = MongoDriver()
    val parsedUri = MongoConnection.parseURI(mongoUri)
    val connection = parsedUri.map(driver.connection(_))

    val futureConnection = Future.fromTry(connection)

    def db1: Future[DefaultDB] = futureConnection.flatMap(_.database("mydb"))
    db1.map(_.collection("person"))
  }
}
