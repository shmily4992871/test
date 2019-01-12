package com.example.practice.functions.impl

import com.example.practice.ServerMain._
import com.example.practice.domain.ID
import com.example.practice.domain.PersonModel._
import com.example.practice.domain.db.DBPerson
import com.example.practice.functions.PersonRepo
import com.example.practice.functions.PersonRepo._
import com.example.practice.util.PipeOperator._
import com.github.mehmetakiftutuncu.errors.Maybe
import com.twitter.util.Future
import io.catbird.util.Rerunnable
import io.github.hamsters.Validation._
import io.github.hamsters.twitter.Implicits._
import javax.inject.{Inject, Singleton}
import mouse.boolean._
import mouse.option._
import perfolation._
import reactivemongo.api.collections.bson.BSONCollection
import reactivemongo.bson.{BSONDocument, BSONDocumentReader, BSONDocumentWriter, Macros}

@Singleton
class PersonMongoRepo @Inject()(personColl: Future[Maybe[BSONCollection]]) extends PersonRepo[Rerunnable] {
  implicit val personWriter: BSONDocumentWriter[DBPerson] = Macros.writer[DBPerson]
  implicit val personReader: BSONDocumentReader[DBPerson] = Macros.reader[DBPerson]

  def add(person: Person): Rerunnable[InsertResult] =
    Rerunnable.fromFuture(personColl.flatMap {
      case OK(coll) =>
        personToDBPerson(person).|>(p => coll.insert[DBPerson](ordered = false).one(p)).flatMap { wr =>
          wr.ok.fold(
            Future.value(InsertSuccess(person.id)),
            Future.value(
              InsertFailure(
                p"Insert a person [name: ${person.name.d}] failed, reason: ${wr.writeErrors.mkString("\n")}"
              )
            )
          )
        }
      case KO(e) => Future.value(InsertFailure(p"[Add Person] error: ${e.represent(includeWhen = true)}"))
    })

  def delete(personId: ID): Rerunnable[DeleteResult] = {
    val selector = BSONDocument("id" -> personId.id.toString())

    Rerunnable.fromFuture(personColl.flatMap {
      case OK(coll) =>
        coll.findAndRemove[BSONDocument](selector).flatMap { fmw =>
          fmw
            .result[DBPerson]
            .cata(
              p => Future.value(DeleteSuccess(dbPersonToPerson(p))),
              Future.value(DeleteFailure(s"Delete a person [id: ${personId.id
                .toString()}], reason: ${fmw.lastError.cata(_.err.cata(identity, "Unknown error"), "Unknown error")}"))
            )
        }
      case KO(e) => Future.value(DeleteFailure(p"[Add Person] error: ${e.represent(includeWhen = true)}"))
    })
  }

  private[this] def personToDBPerson(p: Person): DBPerson =
    DBPerson(p.id.id.toString(), p.name.d, p.age.d, p.gender.asString, p.address.d, p.createTime.d)

  private[this] def dbPersonToPerson(p: DBPerson): Person =
    Person(
      ID.fromString(p.id),
      Name(p.name),
      Age(p.age),
      (p.gender == "male").fold(Male, Female),
      Address(p.address),
      CreateTime(p.create_time)
    )
}
