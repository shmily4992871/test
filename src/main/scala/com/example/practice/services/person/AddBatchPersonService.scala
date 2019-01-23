package com.example.practice.services.person

import com.example.practice.domain.PersonModel.Person
import com.example.practice.functions.PersonRepo.{InsertBatchFailure, InsertBatchSuccess}
import com.example.practice.functions.impl.PersonMongoRepo
import com.example.practice.services.RerunnableService
import io.catbird.util.Rerunnable
import javax.inject.{Inject, Singleton}

@Singleton
class AddBatchPersonService @Inject()(repo: PersonMongoRepo) extends RerunnableService[List[Person], Int] {
  def apply(ps: List[Person]): Rerunnable[Int] =
    repo.addBatch(ps).flatMap {
      case InsertBatchSuccess(count) => Rerunnable.const(count)
      case InsertBatchFailure(msg)   => Rerunnable.raiseError(new Error(msg))
    }
}
