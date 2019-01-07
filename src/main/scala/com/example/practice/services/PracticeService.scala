package com.example.practice.services

import com.example.practice.domain.db.Person
import com.example.practice.modules.MongoDBMudule
import reactivemongo.api.Cursor
import reactivemongo.api.collections.bson.BSONCollection
import reactivemongo.api.commands.{MultiBulkWriteResult, WriteResult}
import reactivemongo.bson.{BSONDocument, BSONDocumentReader, BSONDocumentWriter, Macros}

import scala.concurrent.Future
import scala.util.{Failure, Success}

class PracticeService {

  def collection: Future[BSONCollection] = MongoDBMudule.getCollection()
  import scala.concurrent.ExecutionContext.Implicits.global


  implicit def personWriter: BSONDocumentWriter[Person] = Macros.writer[Person]
  //OK
  /*def insertPerson(person: Person): Future[Unit] = {
    collection.flatMap(_.insert(person).map(_ =>{}))
  }*/

  // Simple: .insert[T].one(t)
  def insertPerson(person: Person): Future[Unit] = {
    val writeRes: Future[WriteResult] = collection.flatMap(_.insert[Person](ordered = false).one(person))

    writeRes.onComplete { // Dummy callbacks
      case Failure(e) => e.printStackTrace()
      case Success(writeResult) =>
        println(s"successfully inserted document with result: $writeResult")
    }

    writeRes.map(_ => {})// After the insertion is successful, you can do something here.
  }

  // Bulk: .insert[T].many(Seq(t1, t2, ..., tN))
  def bulkInsert(personList : List[Person]): Future[Unit] = {
    val writeRes: Future[MultiBulkWriteResult] =
      collection.flatMap(_.insert[Person](ordered = false).
        many(personList))

    writeRes.onComplete { // Dummy callbacks
      case Failure(e) => e.printStackTrace()
      case Success(writeResult) =>
        println(s"successfully inserted document with result: $writeResult")
    }

    writeRes.map(_ => {}) // After the insertion is successful, you can do something here.
  }

  //OK
  def updatePerson(person: Person) : Future[Int] = {

    val selector = BSONDocument("name" -> person.name)
    collection.flatMap(_.update(selector, person).map(_.n))

  }

  def deletePerson(person: Person) : Unit = {
    val selector = BSONDocument("name" -> person.name)

    val futureRemove =
      collection.flatMap(_.delete[BSONDocument](ordered = false).one(selector))

    futureRemove.onComplete { // callback
      case Failure(e) => throw e
      case Success(writeResult) => println("successfully removed document: " + writeResult)
    }
  }

  //OK
  def findAndRemovePerson(person: Person) : Future[Option[Person]] =
    collection.flatMap(_.findAndRemove(BSONDocument("name" -> person.name)).map(_.result[Person]))


  implicit def personReader: BSONDocumentReader[Person] = Macros.reader[Person]
  //OK
  def queryPerson(name: String): Future[List[BSONDocument]] = {

    val selector = BSONDocument("name" -> name)

    val projection : Option[BSONDocument]  = Some(BSONDocument("name" -> 1, "age" -> 1))

    collection.flatMap(_.find(selector, projection).
      cursor[BSONDocument]().
      collect[List](-1, Cursor.FailOnError[List[BSONDocument]]()))
  }
}
