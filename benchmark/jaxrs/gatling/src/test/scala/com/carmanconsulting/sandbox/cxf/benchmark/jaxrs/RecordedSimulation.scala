package com.carmanconsulting.sandbox.cxf.benchmark.jaxrs

import io.gatling.core.Predef._
import io.gatling.http.Predef._

import scala.concurrent.duration._
import scala.language.postfixOps

class RecordedSimulation extends Simulation {

  val httpProtocol = http
    .baseURL("http://localhost:7777")
    .inferHtmlResources()
    .acceptHeader("text/plain")
    .acceptEncodingHeader("gzip, deflate, sdch")

  val scn = scenario("hello").repeat(1000) {
    exec(http("request_0")
      .get("/cxf/hello/jcarman"))
  }

  setUp(scn.inject(rampUsers(500) over (10 seconds)))
    .protocols(httpProtocol)
}