package com.example.practice.controllers

import com.example.practice.Server
import com.example.practice.docker.DockerMongodbService
import com.example.practice.domain.PersonModel.Person
import com.twitter.finatra.http.EmbeddedHttpServer
import com.twitter.inject.server.FeatureTest
import com.whisk.docker.scalatest.DockerTestKit
import com.twitter.finagle.http.Status._
import com.twitter.finatra.json.FinatraObjectMapper
import com.example.practice.util.PipeOperator._
import perfolation._

// sbt 'testOnly com.example.practice.controllers.PracticeControllerTest'
class PracticeControllerTest extends FeatureTest with DockerTestKit with DockerMongodbService {
  startAllOrFail()

  override val server = new EmbeddedHttpServer(new Server)

  override def beforeAll(): Unit = isContainerReady(mongodbContainer).futureValue shouldBe true

  /*test("Server#query ok") {
    server.httpGet(path = "/person/query?personId=135")
  }*/

  test("Should be able to successfully insert and delete an user") {
    val objectMapper = injector.instance[FinatraObjectMapper]

    val pId = server
      .httpPost(
        path = "/person/insert",
        postBody = """
        {
          "name": "zhangsan",
          "age" : 28,
          "gender" : "male",
          "address" : "shenzhen"
        }
        """,
        andExpect = Created
      )
      .contentString
      .|>(c => objectMapper.parse[PersonId](c).id)

    server.httpGet(path = p"/person/query/$pId")

    server.httpDelete(path = p"/person/deleteOne/$pId", andExpect = Ok)
  }

  test("Should be able to successfully insertBatch users") {
    val objectMapper = injector.instance[FinatraObjectMapper]

    server
      .httpPost(
        path = "/person/bulkInsert",
        postBody = """
        [
          {
            "name": "wangwu",
            "age" : 60,
            "gender" : "male",
            "address" : "chengdu",
            "create_time" : 20190104
          },
          {
             "name": "lisi",
             "age" : 8,
             "gender" : "female",
             "address" : "chengdu",
             "create_time" : 20190104
           }
        ]
        """
      )
      .contentString
      .|>(c => objectMapper.parse[PersonCount](c).count)
  }

  test("update person ok") {
    server.httpPost(
      path = "/person/update",
      postBody = """
        {
          "id"      : "bh3sao7h0srh7d944hag",
          "name"    : "famous",
          "age"     : 60,
          "gender"  : "female",
          "address" : "shanghai"
        }
        """
    )
  }

//  test("Server#delete person ok") {
//    server.httpPost(
//      path = "/person/deleteOne",
//      postBody = """
//        {
//          "name": "zhangsan",
//          "age" : 28,
//          "gender" : "male",
//          "address" : "shenzhen",
//          "create_time" : 19921212
//        }
//        """
//    )
//  }
  /*test("Server#delete person ok") {
    server.httpPost(
      path = "/person/findAndRemovePerson",
      postBody = """
        {
          "name": "zhangsan",
          "age" : 32,
          "gender" : "female",
          "address" : "chengdu-yibin",
          "create_time" : 19920204
        }
        """
    )
  }*/
}

final case class PersonId(id: String)    extends AnyVal
final case class PersonCount(count: Int) extends AnyVal
