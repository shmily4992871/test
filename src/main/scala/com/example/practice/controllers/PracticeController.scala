package com.example.practice.controllers

import com.example.practice.domain.db.Person
import com.example.practice.services.PracticeService
import com.twitter.finagle.http.Request
import com.twitter.finatra.http.Controller
import reactivemongo.bson.{BSONDocumentReader, Macros}

import scala.concurrent.ExecutionContext

class PracticeController extends Controller {

  val server = new PracticeService()

  get("/person/query") { request: Request =>
    info("hi")
    //"Hello " + request.params.getOrElse("name", "unnamed")
    server.queryPerson(request.params.getOrElse("name", " "))
  }

  post("/person/insert") { person: Person =>
    server.insertPerson(person)
  }

  post("/person/update") { person: Person =>
    server.updatePerson(person)
  }

  post("/person/delete") { person: Person =>
    server.removedPerson(person)
  }



}
