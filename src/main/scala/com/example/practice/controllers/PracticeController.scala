package com.example.practice.controllers

import com.example.practice.domain.ID
import com.example.practice.domain.PersonModel._
import com.example.practice.models.http._
import com.example.practice.services.person._
import com.twitter.finagle.http.Request
import com.twitter.finatra.http.Controller
import com.twitter.util.Time
import javax.inject.Inject
import perfolation._

// TODO -Need Swagger doc
// W1606004670
class PracticeController @Inject()(addPersonSvc: AddPersonService,
                                   addBatchPersonSvc: AddBatchPersonService,
                                   deletePersonSvc: DeletePersonService,
                                   queryPersonSvc: QueryPersonService,
                                   updateRersonSvc: UpdatePersonService)
    extends Controller {

  get("/person/query:pid") { req: QueryOneRequest =>
    queryPersonSvc(ID.fromString(req.pid))
  }

  post("/person/insert") { person: PersonRequest =>
    addPersonSvc(
      Person(
        id = ID(),
        name = Name(person.name),
        age = Age(person.age),
        gender = person.gender.toLowerCase() match {
          case "male"   => Male
          case "female" => Female
          case _        => Unknow
        },
        address = Address(person.address),
        createTime = CreateTime(Time.now.inMilliseconds)
      )
    ).map(d => response.created.json(IdResponse(d.id.toString()))).run.handle {
      case e: Throwable =>
        // DEBUG
        error(p"[/person/insert] - ${e.getMessage}")

        response.internalServerError
    }
  }

  post("/person/bulkInsert") { list: List[PersonRequest] =>
    val createTime = CreateTime(Time.now.inMilliseconds)

    addBatchPersonSvc(
      list.map(
        person =>
          Person(
            id = ID(),
            name = Name(person.name),
            age = Age(person.age),
            gender = person.gender.toLowerCase() match {
              case "male"   => Male
              case "female" => Female
              case _        => Unknow
            },
            address = Address(person.address),
            createTime = createTime
        )
      )
    ).map(count => response.created.json(InsertNumRequest(count.toString()))).run.handle {
      case e: Throwable =>
        // DEBUG
        error(p"[/person/bulkInsert] - ${e.getMessage}")

        response.internalServerError
    }
  }

  post("/person/update") { person: UpdatePersonRequest =>
    updateRersonSvc(
      UpdatePerson(
        id = ID.fromString(person.id),
        name = Name(person.name),
        age = Age(person.age),
        gender = person.gender.toLowerCase() match {
          case "male"   => Male
          case "female" => Female
          case _        => Unknow
        },
        address = Address(person.address)
      )
    )
  }

  delete("/person/deleteOne/:pid") { req: DeleteOneRequest =>
    deletePersonSvc(ID.fromString(req.pid))
      .map { p =>
        // DEBUG
        info(p"Person: [$p] was deleted")

        response.ok
      }
      .run
      .handle {
        case e: Throwable =>
          // DEBUG
          error(p"[/person/deleteOne/${req.pid}] - ${e.getMessage}")

          response.internalServerError
      }
  }

  post("/person/findAndRemovePerson") { req: Request =>
    response.notImplemented
  }

}
