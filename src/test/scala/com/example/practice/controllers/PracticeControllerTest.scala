package com.example.practice.controllers
import com.example.practice.Server
import com.twitter.finatra.http.EmbeddedHttpServer
import com.twitter.inject.server.FeatureTest

class PracticeControllerTest extends FeatureTest {
  override val server = new EmbeddedHttpServer(new Server)

  test("Server#query ok") {
    server.httpGet(path = "/person/query?name=tom")
  }

  /*test("Server#save person ok") {
    server.httpPost(
      path = "/person/insert",
      postBody = """
        {
          "name": "testName",
          "age" : 18,
          "gender" : "female",
          "address" : "yibin",
          "create_time" : 19920210
        }
        """
    )
  }*/

  /*test("Server#Say hi for Post") {
    server.httpPost(
      path = "/person/update",
      postBody = """
        {
          "name": "tom",
          "age" : 32,
          "gender" : "female",
          "address" : "chengdu-yibin",
          "create_time" : 19920204
        }
        """
    )
  }*/

  /*test("Server#delete person ok") {
    server.httpPost(
      path = "/person/delete",
      postBody = """
        {
          "name": "testName",
          "age" : 32,
          "gender" : "female",
          "address" : "chengdu-yibin",
          "create_time" : 19920204
        }
        """
    )
  }*/

}
