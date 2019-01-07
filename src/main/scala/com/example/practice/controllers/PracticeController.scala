package com.example.practice.controllers

import com.example.practice.domain.db.Person
import com.example.practice.services.PracticeService
import com.twitter.finagle.http.Request
import com.twitter.finatra.http.Controller

class PracticeController extends Controller {

  val server = new PracticeService()

  get("/person/query") { request: Request =>
    info("hi")
    server.queryPerson(request.params.getOrElse("name", " "))
  }

  post("/person/insert") { person: Person =>
    server.insertPerson(person)
  }

  post("/person/bulkInsert") { list : List[Person] =>
    server.bulkInsert(list)
  }

  post("/person/update") { person: Person =>
    server.updatePerson(person)
  }

  post("/person/deleteOne") { person: Person =>
    server.deletePerson(person)
  }

  post("/person/findAndRemovePerson") { person: Person =>
    server.findAndRemovePerson(person)
  }

}
