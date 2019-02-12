package com.example.practice.models.http

import com.twitter.finatra.request.RouteParam

final case class DeleteOneRequest(@RouteParam pid: String) extends AnyVal
