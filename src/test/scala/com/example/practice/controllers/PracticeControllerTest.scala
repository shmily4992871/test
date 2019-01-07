package com.example.practice.controllers
import com.example.practice.Server
import com.twitter.finatra.http.EmbeddedHttpServer
import com.twitter.inject.server.FeatureTest

class PracticeControllerTest extends FeatureTest {
  override val server = new EmbeddedHttpServer(new Server)

  /*test("Server#query ok") {
    server.httpGet(path = "/person/query?name=tom")
  }*/

  /*test("Server#save person ok") {
    server.httpPost(
      path = "/person/insert",
      postBody = """
        {
          "name": "zhangsan",
          "age" : 28,
          "gender" : "male",
          "address" : "shenzhen",
          "create_time" : 19921212
        }
        """
    )
  }*/

  /*test("Server#save person ok") {
    server.httpPost(
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
  }*/

  /*test("Server#update person ok") {
    server.httpPost(
      path = "/person/update",
      postBody = """
        {
          "name": "famous",
          "age" : 60,
          "gender" : "female",
          "address" : "shanghai",
          "create_time" : 19980808
        }
        """
    )
  }*/

  test("Server#delete person ok") {
    server.httpPost(
      path = "/person/deleteOne",
      postBody = """
        {
          "name": "zhangsan",
          "age" : 28,
          "gender" : "male",
          "address" : "shenzhen",
          "create_time" : 19921212
        }
        """
    )
  }


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
