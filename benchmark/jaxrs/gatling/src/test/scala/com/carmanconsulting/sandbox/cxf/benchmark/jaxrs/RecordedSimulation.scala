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

  val repeat = System.getProperty("repeat", "1000")
  val users = System.getProperty("users", "500")
  val duration = System.getProperty("duration", "10")

  val scn = scenario("hello").repeat(repeat.toInt) {
    exec(http("request_0")
      .get("/cxf/hello/jcarman"))
  }


  setUp(scn.inject(rampUsers(users.toInt) over (duration.toInt seconds)))
    .protocols(httpProtocol)
}