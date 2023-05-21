package com.tset.nft.requests.whenwise

import io.gatling.core.Predef._
import io.gatling.core.structure._
import io.gatling.http.Predef._

object auth {

  /**
   * This method performs a simple GET to the sign-in endpoint.
   * This endpoint returns an HTML document, from which we need the 'csrf-token'. This is extracted using CSS selectors
   * The extracted token is then saved in session for later use
   */
  def getAuth: ChainBuilder =
    exec(
      http("Get Auth Token")
        .get("/sign-in")
        .check(status.is(200))
        .check(css("meta[name='csrf-token']", "content").saveAs("token"))
    )
}
