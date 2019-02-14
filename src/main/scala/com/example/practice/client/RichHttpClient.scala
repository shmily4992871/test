package com.example.practice.client

import com.example.practice.util.AppConfigLib._
import com.example.practice.util.PipeOperator._
import com.twitter.conversions.DurationOps._
import com.twitter.finagle.http.{Request, Response}
import com.twitter.finagle.{Http, Service}
import mouse.boolean._

object RichHttpClient {
  /* Public */
  private[this] val shouldEnableFastFail =
    getConfig[Boolean]("FAIL_FAST_ENABLE").getOrElse(false)

  def newClientService(dest: String): Service[Request, Response] =
    Http.client.withSession
      .maxLifeTime(20.seconds)
      .withSession
      .maxIdleTime(10.seconds)
      .|>(c => (!shouldEnableFastFail).fold(c.withSessionQualifier.noFailFast, c))
      .newService(dest)

  def newSslClientService(sslHostname: String, dest: String): Service[Request, Response] = {
    val shouldValidate = getConfig[Boolean]("TLS_VALIDATION").getOrElse(true)

    Http.client.withSession
      .maxLifeTime(20.seconds)
      .withSession
      .maxIdleTime(10.seconds)
      .|>(c => (!shouldEnableFastFail).fold(c.withSessionQualifier.noFailFast, c))
      .|>(c => shouldValidate.fold(c.withTls(sslHostname), c.withTlsWithoutValidation))
      .newService(dest)
  }
}
