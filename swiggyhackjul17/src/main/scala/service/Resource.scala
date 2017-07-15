package service

/**
  *
  * Created by Rishabh on 15/07/17.
  */

import javax.ws.rs._
import javax.ws.rs.core.{MediaType, Response}

@Path("/")
class Resource {

  @GET
  @Path("/pickup/")
  @Produces(Array(MediaType.APPLICATION_JSON))
  def getProfile(@QueryParam("pickUpLat") pickUpLat: Double,
                @QueryParam("pickUpLng") pickUpLng: Double,
                @QueryParam("restaurantId") restaurantId: Int): Response = {

    val json =
      """{
        |"status": "ok",
        |"bestPickUpTime": "15:30"
        |}""".stripMargin

    Response.status(200).entity(json)
      .header("Access-Control-Allow-Methods", "POST, GET, OPTIONS, PUT, DELETE")
      .header("Access-Control-Allow-Origin","*")
      .build()
  }

}

