package com.example.practice.services.person

import com.example.practice.domain.ID
import com.example.practice.domain.PersonModel.Person
import com.example.practice.functions.PersonRepo.{DeleteFailure, DeleteSuccess}
import com.example.practice.functions.impl.PersonMongoRepo
import com.example.practice.services.RerunnableService
import io.catbird.util.Rerunnable
import javax.inject.{Inject, Singleton}

@Singleton
class DeletePersonService @Inject()(repo: PersonMongoRepo) extends RerunnableService[ID, Person] {
  def apply(id: ID): Rerunnable[Person] = repo.delete(id).flatMap {
    case DeleteSuccess(p)   => Rerunnable.const(p)
    case DeleteFailure(msg) => Rerunnable.raiseError(new Error(msg))
  }
}
