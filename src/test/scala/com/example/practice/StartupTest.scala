package com.example.practice

import com.example.practice.docker.DockerMongodbService
import com.google.inject.Stage
import com.twitter.finatra.http.EmbeddedHttpServer
import com.twitter.inject.server.FeatureTest
import com.whisk.docker.scalatest.DockerTestKit

class StartupTest extends FeatureTest with DockerTestKit with DockerMongodbService {
  startAllOrFail()

  val server = new EmbeddedHttpServer(stage = Stage.PRODUCTION, twitterServer = new Server)

  override def beforeAll(): Unit = isContainerReady(mongodbContainer).futureValue shouldBe true

  test("server") {
    server.assertHealthy()
  }
}
