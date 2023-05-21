package com.tset.nft.requests.petstore

import io.gatling.core.Predef._
import io.gatling.core.structure._
import io.gatling.http.Predef._

object auth {
  /**
   * This request performs an Auth request to the oauth/login endpoint.
   * This is going to place an Authorization cookie in the session which will be shared across all subsequent requests
   */
  val getAuthCookie : ChainBuilder = {
    exec(
      http("Get Authorisation Cookie")
        .post("/oauth/login")
        .formParam("username", "test")
        .formParam("password", "abc123")
        .formParam("login", "")
        .check(status.is(302))
        .disableFollowRedirect
    )

  }

}
