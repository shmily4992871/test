package com.example.practice.controllers

import com.example.practice.domain.ID
import com.example.practice.domain.PersonModel._
import com.example.practice.domain.db.DBPerson
import com.example.practice.models.http.{DeleteOneRequest, IdResponse, PersonRequest}
import com.example.practice.services.person.{AddPersonService, DeletePersonService}
import com.twitter.finagle.http.Request
import com.twitter.finatra.http.Controller
import com.twitter.util.Time
import javax.inject.Inject
import mouse.boolean._
import perfolation._

// TODO -Need Swagger doc
// W1606004670
class PracticeController @Inject()(addPersonSvc: AddPersonService, deletePersonSvc: DeletePersonService)
    extends Controller {

  get("/person/query") { request: Request =>
    response.notImplemented
  }

  post("/person/insert") { person: PersonRequest =>
    addPersonSvc(
      Person(
        id = ID(),
        name = Name(person.name),
        age = Age(person.age),
        gender = (person.gender == "male").fold(Male, Female),
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

  post("/person/bulkInsert") { list: List[DBPerson] =>
    response.notImplemented
  }

  post("/person/update") { req: Request =>
    response.notImplemented
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
