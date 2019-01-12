package com.example.practice.functions

import com.example.practice.domain.ID
import com.example.practice.domain.PersonModel.Person
import com.example.practice.functions.PersonRepo.{DeleteResult, InsertResult}

trait PersonRepo[F[_]] {
  def add(person: Person): F[InsertResult]
  def delete(personId: ID): F[DeleteResult]
}

object PersonRepo {
  sealed trait InsertResult                   extends Product with Serializable
  final case class InsertSuccess(id: ID)      extends InsertResult
  final case class InsertFailure(msg: String) extends InsertResult

  sealed trait DeleteResult             extends Product with Serializable
  case class DeleteSuccess(p: Person)   extends DeleteResult
  case class DeleteFailure(msg: String) extends DeleteResult
}
