package com.example.practice.services.person

import com.example.practice.domain.ID
import com.example.practice.domain.PersonModel.Person
import com.example.practice.functions.PersonRepo.{QueryFailure, QuerySuccess}
import com.example.practice.functions.impl.PersonMongoRepo
import com.example.practice.services.RerunnableService
import io.catbird.util.Rerunnable
import javax.inject.{Inject, Singleton}

@Singleton
class QueryPersonService @Inject()(repo: PersonMongoRepo) extends RerunnableService[ID, Person] {
  override def apply(personId: ID): Rerunnable[Person] =
    repo.query(personId).flatMap {
      case QuerySuccess(person) => Rerunnable.const(person)
      case QueryFailure(msg)    => Rerunnable.raiseError(new Error(msg))
    }
}
