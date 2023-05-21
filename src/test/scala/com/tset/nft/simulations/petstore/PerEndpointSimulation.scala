package com.tset.nft.simulations.petstore

import com.tset.nft.helpers.SimulationSettings
import com.tset.nft.requests.petstore.pet._
import com.tset.nft.scenarios.AddAndUpdatePet
import io.gatling.core.Predef._
import io.gatling.core.structure.ScenarioBuilder

import scala.concurrent.duration.DurationInt
import scala.util.Random

class PerEndpointSimulation extends Simulation with SimulationSettings{
  /**
   * This is the injection strategy, or in Gatling terms, a Simulation
   * Here we declare the manner in which we'll introduce load into the system.
   *
   * This simulation is a little different from the others, as we want to exercise each endpoint independently
   * We're declaring each scenario in-line, including only one request each.
   *
   * For each scenario, we'll be injecting load in the following manner:
   * 1. Reach 200 requests per second (rps) in 1 minute (evenly ramped)
   * 2. Then keep current rps for 1 minute
   * 3. Then reach 400 rps in 1 minute (evenly ramped)
   * 4. Then keep current rps for 1 minute
   * Run duration: 1 + 1 + 1 + 1 = 4 minutes
   *
   * Unfortunately we'll hardcode some values as normally each request depends on the response of a previous request.
   * In this case, we're hardcoding:
   * The petId for the GET request (meaning we'll always GET the same pet).
   * This is not optimal for a proper performance test, but it doesn't appear ids are assigned sequentially.
   *
   * Most of the updatePet (PUT) body, except for the status which is being changed randomly.
   * (meaning we'll always update the same pet)
   * This is also not optimal, but it's the simplest solution in the interest of time.
   */

  val petId: String = "9223372036854775807"

  setUp(
    scenario("Add Pet")
      .forever(exec(addPet))
      .inject(
        atOnceUsers(133))
      .throttle(
        reachRps(200).in(1.minutes),
        holdFor(1.minutes),
        reachRps(400).in(1.minutes),
        holdFor(1.minutes)
      ),
    scenario("Get Pet")
      .forever(exec(getPet(petId)))
      .inject(
        atOnceUsers(133))
      .throttle(
          reachRps(200).in(1.minutes),
          holdFor(1.minutes),
          reachRps(400).in(1.minutes),
          holdFor(1.minutes)
      ),
    scenario("Update Pet")
      .forever(exec(updatePet(useHardcoded = true)))
      .inject(
        atOnceUsers(133))
      .throttle(
          reachRps(200).in(1.minutes),
          holdFor(1.minutes),
          reachRps(400).in(1.minutes),
          holdFor(1.minutes)
      )
  )
  .protocols(httpProtocol)
  .maxDuration(4.minutes)

}
