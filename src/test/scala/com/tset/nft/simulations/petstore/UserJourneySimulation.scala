package com.tset.nft.simulations.petstore

import com.tset.nft.helpers.SimulationSettings
import com.tset.nft.scenarios.AddAndUpdatePet
import io.gatling.core.Predef._

import scala.concurrent.duration.DurationInt

class UserJourneySimulation extends Simulation with SimulationSettings{
  /**
   * This is the injection strategy, or in Gatling terms, a Simulation
   * Here we declare the manner in which we'll introduce load into the system.
   * For this particular sample, we'll introduce concurrent users from 0 to 400 in the following steps:
   * 1. Reach 200 users in 1 minute (evenly ramped)
   * 2. Then keep current users for 1 minute
   * 3. Then reach 400 users in 1 minute (evenly ramped)
   * 4. Then keep current users for 1 minute
   * Run duration: 1 + 1 + 1 + 1 = 4 minutes
   */
  setUp(
    AddAndUpdatePet.addAndUpdatePetScenario
      .inject(
        rampUsers(200) during 1.minutes,
        nothingFor(1.minutes),
        rampUsers(200) during 1.minutes,
        nothingFor(1.minutes)
      )
      .protocols(httpProtocol)
  ).maxDuration(4.minutes)

}
