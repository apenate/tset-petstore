package com.tset.nft.simulations.petstore

import com.tset.nft.helpers.SimulationSettings
import com.tset.nft.scenarios.AddAndUpdatePet
import io.gatling.core.Predef._

import scala.concurrent.duration.DurationInt

class SmokeSimulation extends Simulation with SimulationSettings{

  /**
   * This is a "smoke" simulation. This will inject a single user, then exit after 10 seconds.
   * Useful to determine if our test is working as expected
   */
  setUp(
    AddAndUpdatePet.addAndUpdatePetSmokeScenario
      .inject(
        atOnceUsers(1)
      )
      .protocols(httpProtocol)
  )
}
