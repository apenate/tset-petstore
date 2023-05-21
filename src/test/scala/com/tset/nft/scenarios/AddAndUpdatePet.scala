package com.tset.nft.scenarios

import com.tset.nft.requests.petstore.pet
import io.gatling.core.Predef._
import io.gatling.core.structure._

object AddAndUpdatePet {
  /**
   * We're declaring a Scenario here, which is basically a User Journey. In other terms, which requests will be sent
   * and in what order.
   * Since this will be looped, we're performing a session reset at the beginning to clear any session variables
   */
  val addAndUpdatePetScenario : ScenarioBuilder =
    scenario("Add, Get then Update a Pet")
      .forever(
        exec(_.reset)
        .exec(
          pet.addPet,
          pet.getPet(petId = "${petId}"),
          pet.updatePet
        )
        .pause(2, 3)
    )

  /**
   * This scenario is similar to the one above, with the difference that it will only loop once.
   * This will be used as a smoke test for our code.
   */
  val addAndUpdatePetSmokeScenario: ScenarioBuilder =
    scenario("Add, Get then Update a Pet - Smoke Test")
      .exec(
        pet.addPet,
        pet.getPet(petId = "${petId}"),
        pet.updatePet
      )
}
