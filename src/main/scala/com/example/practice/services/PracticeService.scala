package com.example.practice.services

import com.example.practice.domain.db.Person
import com.example.practice.modules.MongoDBMudule
import reactivemongo.api.Cursor
import reactivemongo.api.collections.bson.BSONCollection
import reactivemongo.api.commands.WriteResult
import reactivemongo.bson.{BSONDocument, BSONDocumentReader, BSONDocumentWriter, Macros, document}

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

class PracticeService {

  def collection:Future[BSONCollection] = MongoDBMudule.getCollection()
  import scala.concurrent.ExecutionContext.Implicits.global


  //OK
  implicit def personWriter: BSONDocumentWriter[Person] = Macros.writer[Person]
  def insertPerson(person: Person): Future[Unit] = {
    collection.flatMap(_.insert(person).map(_ =>{}))
  }

  //OK
  def updatePerson(person: Person) : Future[Int] = {
    val selector = BSONDocument("name" -> person.name)
    collection.flatMap(_.update(selector, person).map(_.n))
  }

  //OK
  def removedPerson(person: Person) : Future[Option[Person]] =
    collection.flatMap(_.findAndRemove(BSONDocument("name" -> person.name)).map(_.result[Person]))

  implicit def personReader: BSONDocumentReader[Person] = Macros.reader[Person]
  //OK
  /*def queryPerson(name: String) : Future[Option[BSONDocument]] = {
    val selector = BSONDocument("name" -> name)
    collection.flatMap(_.find(selector).one[BSONDocument])
  }*/
  def queryPerson(name: String) : Future[List[Person]] =
    collection.flatMap(_.find(document("name" -> name)).
      cursor[Person]().
      collect[List](-1, Cursor.FailOnError[List[Person]]()))
}
