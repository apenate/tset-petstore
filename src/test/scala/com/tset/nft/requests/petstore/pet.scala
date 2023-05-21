package com.tset.nft.requests.petstore

import io.gatling.core.Predef._
import io.gatling.core.feeder.BatchableFeederBuilder
import io.gatling.core.structure._
import io.gatling.http.Predef._

import scala.util.Random

/**
 * Pet related requests
 */
object pet {

  /**
   * This method creates a new pet.
   * With this method, we're showcasing the use of a File Based feeder, from which we're getting pre-defined pet names
   * and photo URL. This can be useful if there's a defined set of data we want to use on our tests. For example, we can
   * randomly rotate through test usernames
   *
   * The method sends a POST request, using the "blueprint" from the addPet.json under resources/bodies
   * Then, we ensure we get a HTTP 200 response, and from the response body, extract the id, which may be used for
   * subsequent requests
   */
  val addPet: ChainBuilder = {
    val petDataFeeder: BatchableFeederBuilder[String] = csv("data/petstore/petData.csv").circular.random

    feed(petDataFeeder)
    .exec(
      http("Add Pet")
        .post("/pet/")
        .body(ElFileBody("bodies/addPet.json")).asJson
        .check(status.is(200))
        .check(jsonPath("$.id").saveAs("petId"))
    )

  }

  /**
   * This method gets an existing pet.
   * @param petId The id of the existing pet. If the id is already in memory due to the previous execution of [[addPet]]
   *              method, we can pass the Gatling EL string ${sessionAttribute}.
   *
   * The method sends a simple GET request with no query string.
   * Then, we ensure we get a HTTP 200 response, and from the response body, extract ALL fields, which may be used for
   * subsequent requests
   *
   */
  def getPet(petId: String): ChainBuilder = {
    exec(
      http("Get Pet")
        .get(s"/pet/${petId}")
        .check(status.is(200))
        .check(jsonPath("$.id").saveAs("petId"))
        .check(jsonPath("$.category.id").saveAs("categoryId"))
        .check(jsonPath("$.category.name").saveAs("categoryName"))
        .check(jsonPath("$.name").saveAs("petName"))
        .check(jsonPath("$.photoUrls").saveAs("urls"))
        .check(jsonPath("$.tags").saveAs("tags"))
        .check(jsonPath("$.status").saveAs("status"))
    )
  }

  /**
   * This method updates an existing pet.
   *
   * WARNING: Invoking this method without invoking [[getPet]] before will likely result in an error.
   *
   * With this method, we're showcasing the use of a "hardcoded" feeder. This can be useful when there's only a
   * few variations which might not be worth putting on a separate file. This however can be done if so desired.
   * For this particular sample, we're randomly selecting a status which we'll update our pet with.
   * We're also showcasing session parameter string interpolation by updating the name of the pet to Captain [name]
   *
   * We're sending a PUT request using the "blueprint" from the updatePet.json under resources/bodies, using the data
   * obtained from the [[getPet]] method. Then, checking we get a HTTP 200 response
   *
   */
  def updatePet(useHardcoded : Boolean = false): ChainBuilder = {

    def statusFeeder: Iterator[Map[String, String]] = Iterator.continually {
      Map("status" -> Random.shuffle(List("available", "pending", "sold")).head)
    }

    feed(statusFeeder)
    .exec(
      session => {
        if(session.contains("petName"))
          session.set("petName", s"Captain ${session("petName").as[String]}")
        else
          session
      }
    )
    .exec(
      http("Update Pet")
        .put("/pet/")
        .body(ElFileBody(if(useHardcoded) "bodies/updatePetHardcoded.json" else "bodies/updatePet.json")).asJson
        .check(status.is(200))
    )
  }

  val updatePet : ChainBuilder = updatePet()
}
