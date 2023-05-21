package com.tset.nft.helpers

import io.gatling.core.Predef._
import io.gatling.core.controller.throttle.ThrottleStep
import io.gatling.http.Predef._
import io.gatling.http.protocol.HttpProtocolBuilder

import scala.concurrent.duration.DurationInt

/** Used for common setting shared across all simulations.
 *  This being a rather simple test, the only things we're declaring here is the base URL for our system under test, and
 *  also an "injection strategy" which can be inherited to all simulations, so less experienced testers can simply
 *  reuse it. Any other snippets can also be added here to be used across all simulations.
 *
 *  The base URL can be declared via environment variables by passing variable "baseHost" (useful when running in CI/CD)
 *  If this is not declared, then default value will be used
 */
trait SimulationSettings {

  def httpProtocol : HttpProtocolBuilder = http.baseUrl(sys.env.getOrElse("baseHost", "https://petstore.swagger.io/v2"))

  def injectionStrategy_throttle(rps : Int, rampTime: Int, holdTime: Int) : Seq[ThrottleStep] =
    Seq(
      reachRps(rps).in(rampTime.minutes),
      holdFor(holdTime.minutes),
      reachRps(rps).in(rampTime.minutes),
      holdFor(holdTime.minutes)
    )
}
