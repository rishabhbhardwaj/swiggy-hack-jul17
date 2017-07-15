package service

/**
  *
  * Created by Rishabh on 15/07/17.
  */

import javax.ws.rs._
import javax.ws.rs.core.{MediaType, Response}

import db.DBClient
import play.api.libs.json.Json

import scalaj.http._

@Path("/")
class Resource {

  val dBClient: DBClient = new DBClient

  @GET
  @Path("/pickup/")
  @Produces(Array(MediaType.APPLICATION_JSON))
  def getProfile(@QueryParam("pickUpLat") pickUpLat: Double,
                 @QueryParam("pickUpLng") pickUpLng: Double,
                 @QueryParam("restaurantId") restaurantId: Int): Response = {

    val (restLat, restLng) = getRestaurantLocation(restaurantId)
    val time = getTravelTime(pickUpLat, pickUpLng, restLat, restLng)

    val json =
      s"""{
        |"status": "ok",
        |"bestPickUpTime": $time
        |}""".stripMargin


    Response.status(200).entity(json)
      .header("Access-Control-Allow-Methods", "POST, GET, OPTIONS, PUT, DELETE")
      .header("Access-Control-Allow-Origin", "*")
      .build()
  }

  private def getRestaurantLocation(restaurantId: Int): (Double, Double) = {
    dBClient.getRestaurantLocation(restaurantId)
  }

  private def getTravelTime(lat1: Double, lng1: Double, lat2: Double, lng2: Double): Double = {

    val url = "https://maps.googleapis.com/maps/api/distancematrix/json"

    val response: HttpResponse[String] = Http(url)
      .option(HttpOptions.allowUnsafeSSL)
//      .param("units", "imperial")
      .param("origins", s"$lat1,$lng2")
      .param("destinations", s"$lat2,$lng2")
      .param("key", "AIzaSyAXoEZxb8sAHKDx-9KcRCfq_U-hEami2bo")
      .asString

    val jsonBody = Json.parse(response.body)

    ((jsonBody \ "rows" \ 0).get \ "elements" \ 0 \ "duration" \ "text").get.toString().split(" " +
      "")(0).replaceAll("\"", "").toDouble
  }
}

