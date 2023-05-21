package com.tset.nft.requests.whenwise

import io.gatling.core.Predef._
import io.gatling.core.structure._
import io.gatling.http.Predef._

object user {
  /**
   * This method sends a POST to the sessions endpoint in order to login
   * WARNING: Token, email and password must be saved in session, otherwise this method will fail
   */
  def login: ChainBuilder =
    exec(
      http("Login")
        .post("/sessions")
        .formParam("session[email]", "${email}")
        .formParam("session[password]", "${password}")
        .formParam("authenticity_token", "${token}")
        .check(status.is(200))
    )

  /**
   * This method performs a simple GET to the sign-out endpoint in order to logout
   */
  def logout: ChainBuilder =
    exec(
      http("Logout")
        .get("/sign-out")
        .check(status.is(200))
    )

}
