package com.example.practice.services.person

import com.example.practice.domain.ID
import com.example.practice.domain.PersonModel.Person
import com.example.practice.functions.PersonRepo.{InsertFailure, InsertSuccess}
import com.example.practice.functions.impl.PersonMongoRepo
import com.example.practice.services.RerunnableService
import io.catbird.util.Rerunnable
import javax.inject.{Inject, Singleton}

@Singleton
class AddPersonService @Inject()(repo: PersonMongoRepo) extends RerunnableService[Person, ID] {
  def apply(p: Person): Rerunnable[ID] =
    repo.add(p).flatMap {
      case InsertSuccess(id)  => Rerunnable.const(id)
      case InsertFailure(msg) => Rerunnable.raiseError(new Error(msg))
    }
}
