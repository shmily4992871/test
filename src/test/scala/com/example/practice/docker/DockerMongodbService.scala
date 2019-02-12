package com.example.practice.docker

import com.whisk.docker.impl.dockerjava.DockerKitDockerJava
import com.whisk.docker.{DockerContainer, DockerReadyChecker}

trait DockerMongodbService extends DockerKitDockerJava {

  val DefaultMongodbPort = 27017

  val mongodbContainer: DockerContainer = DockerContainer("mongo:3.2.17")
    .withPorts(DefaultMongodbPort -> Some(27017))
    .withReadyChecker(DockerReadyChecker.LogLineContains("waiting for connections on port"))
    .withCommand("mongod", "--nojournal", "--smallfiles", "--syncdelay", "0")

  abstract override def dockerContainers: List[DockerContainer] = mongodbContainer :: super.dockerContainers
}
