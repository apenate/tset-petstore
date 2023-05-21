package com.tset.nft.simulations.whenwise

import com.tset.nft.helpers.SimulationSettings
import com.tset.nft.requests.whenwise.auth.getAuth
import com.tset.nft.requests.whenwise._
import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.http.protocol.HttpProtocolBuilder

import scala.concurrent.duration.DurationInt

class LoginWithAuthSimulation extends Simulation with SimulationSettings{
  /**
   * This is a simulation which uses a different baseHost. Therefore, we are overriding the value inherited
   * from SimulationSettings
   *
   * Also note for this simulation we declared the scenario in-line. This is just to showcase Gatling's flexibility, but
   * it's recommended to declare the scenarios on a different file to keep the code organized.
   */

  override val httpProtocol : HttpProtocolBuilder = http.baseUrl("https://whenwise.agileway.net")
  setUp(
        scenario("Login then Logout")
        .exec(_.reset)
        .feed(csv("data/whenwise/userData.csv").random.circular)
        .exec(
          getAuth,
          user.login,
          user.logout
        ).inject(
          rampConcurrentUsers(1) to 100 during 1.minutes
        )
      .protocols(httpProtocol)
  ).maxDuration(60.seconds)


}
