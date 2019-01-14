package com.example.practice.controllers

import com.example.practice.Server
import com.example.practice.docker.DockerMongodbService
import com.twitter.finagle.http.Status._
import com.twitter.finatra.http.EmbeddedHttpServer
import com.twitter.inject.server.FeatureTest
import com.whisk.docker.scalatest.DockerTestKit

class MainControllerFeatureTest extends FeatureTest with DockerTestKit with DockerMongodbService {
  val serviceVersion: String = "0.9.9"

  startAllOrFail()

  override val server: EmbeddedHttpServer =
    new EmbeddedHttpServer(twitterServer = new Server, flags = Map("service.version" -> serviceVersion))

  override def beforeAll(): Unit = isContainerReady(mongodbContainer).futureValue shouldBe true

  test("Server should respond") {
    server.httpGet(path = "/tstmsg/Richard", andExpect = Ok, withJsonBody = """{"message":"Hello, Richard"}""")
    server.httpGet(path = "/tstmsg/anonymous", andExpect = Ok, withJsonBody = """{"message":"Your name, please?"}""")
    server.httpGet(path = "/tstmsg/unknown", andExpect = BadRequest)
  }
}
