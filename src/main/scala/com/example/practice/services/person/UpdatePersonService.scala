package com.example.practice.services.person

import com.example.practice.domain.PersonModel.{Person, UpdatePerson}
import com.example.practice.functions.PersonRepo.{UpdateFailure, UpdateSuccess}
import com.example.practice.functions.impl.PersonMongoRepo
import com.example.practice.services.RerunnableService
import io.catbird.util.Rerunnable
import javax.inject.{Inject, Singleton}

@Singleton
class UpdatePersonService @Inject()(repo: PersonMongoRepo) extends RerunnableService[UpdatePerson, Person] {
  def apply(p: UpdatePerson): Rerunnable[Person] =
    repo.update(p).flatMap {
      case UpdateSuccess(p)   => Rerunnable.const(p)
      case UpdateFailure(msg) => Rerunnable.raiseError(new Error(msg))
    }
}
