package com.example.practice.functions

import com.example.practice.domain.ID
import com.example.practice.domain.PersonModel.{Person, UpdatePerson}
import com.example.practice.functions.PersonRepo._

trait PersonRepo[F[_]] {
  def add(person: Person): F[InsertResult]
  def addBatch(personList: List[Person]): F[InsertBatchResult]
  def delete(personId: ID): F[DeleteResult]
  def update(updatePerson: UpdatePerson): F[UpdateResult]
  def query(personId: ID): F[QueryResult]
}

object PersonRepo {
  sealed trait InsertResult                   extends Product with Serializable
  final case class InsertSuccess(id: ID)      extends InsertResult
  final case class InsertFailure(msg: String) extends InsertResult

  sealed trait InsertBatchResult                   extends Product with Serializable
  final case class InsertBatchSuccess(count: Int)  extends InsertBatchResult
  final case class InsertBatchFailure(msg: String) extends InsertBatchResult

  sealed trait DeleteResult                   extends Product with Serializable
  final case class DeleteSuccess(p: Person)   extends DeleteResult
  final case class DeleteFailure(msg: String) extends DeleteResult

  sealed trait UpdateResult                      extends Product with Serializable
  final case class UpdateSuccess(person: Person) extends UpdateResult
  final case class UpdateFailure(msg: String)    extends UpdateResult

  sealed trait QueryResult                      extends Product with Serializable
  final case class QuerySuccess(person: Person) extends QueryResult
  final case class QueryFailure(msg: String)    extends QueryResult
}
