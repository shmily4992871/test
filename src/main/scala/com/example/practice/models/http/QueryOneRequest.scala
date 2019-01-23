package com.example.practice.models.http
import com.twitter.finatra.request.RouteParam

final case class QueryOneRequest(@RouteParam pid: String) extends AnyVal
