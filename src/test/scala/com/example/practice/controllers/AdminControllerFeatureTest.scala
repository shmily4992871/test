package com.example.practice.controllers

import com.example.practice.Server
import com.example.practice.docker.DockerMongodbService
import com.twitter.finagle.http.Status._
import com.twitter.finatra.http.EmbeddedHttpServer
import com.twitter.inject.server.FeatureTest
import com.whisk.docker.scalatest.DockerTestKit

class AdminControllerFeatureTest extends FeatureTest with DockerTestKit with DockerMongodbService {
  val serviceVersion = "0.9.9"

  startAllOrFail()

  override val server =
    new EmbeddedHttpServer(twitterServer = new Server, flags = Map("service.version" -> serviceVersion))

  override def beforeAll(): Unit = isContainerReady(mongodbContainer).futureValue shouldBe true

  test("Server should return `OK` (health check) if the service is on") {
    server.httpGet(path = "/health", andExpect = Ok)
  }

  test("Server should return service version") {
    server.httpGet(path = "/version", andExpect = Ok, withBody = serviceVersion)
  }
}
