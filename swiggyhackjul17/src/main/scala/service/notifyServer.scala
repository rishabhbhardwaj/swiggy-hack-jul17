package service

import com.pubnub.api.{Callback, Pubnub, PubnubError}
import db.DBClient
import play.api.libs.json.Json

import scalaj.http.{Http, HttpOptions, HttpResponse}

import scala.util.control.Breaks._

/**
  *
  * Created by Rishabh on 16/07/17.
  */

object notifyServer {

  val dBClient = new DBClient

  val PUB_KEY = "pub-c-24317e97-b457-4711-8b10-6dd17aaefa3b"
  val SUB_KEY = "sub-c-8696da1e-69ab-11e7-8127-0619f8945a4f"

  def main(args: Array[String]): Unit = {

    val pubnub = new Pubnub(PUB_KEY, SUB_KEY)
    println("Pubnub Connection Created..")
    //Thread.sleep(5000)

    //notifyDriver(pubnub, "From Java")

    val userLoc = dBClient.getUserLoc()

    println(userLoc.size)

    val pu_lat = userLoc.last._1
    val pu_lng = userLoc.last._2

    val buf_time = -14 //in min

    breakable {
      for (i <- userLoc.indices) {

        print("Current row: " + i)
        if (i < userLoc.size - 1) {

          val cur_lat = userLoc(i)._1
          val cur_lng = userLoc(i)._2

          val nxt_lat = userLoc(i + 1)._1
          val nxt_lng = userLoc(i + 1)._2

          println("curr: "+ cur_lat+" "+cur_lng)
          println("next: "+ nxt_lat+" "+nxt_lng)


          val cur_to_dest = getTravelTime(cur_lat, cur_lng, pu_lat, pu_lng)
          println(".. Current to Dest Time: " + cur_to_dest)
          val cur_to_nxt = getTravelTime(cur_lat, cur_lng, nxt_lat, nxt_lng)
          println(".. Current to Next Time: " + cur_to_nxt)

          val de_to_rest = 2
          val rest_to_pu = 2

          if (de_to_rest + rest_to_pu > cur_to_dest + buf_time) {
            notifyDriver(pubnub, s"You are assigned for orderId: 1")
            notifyUser(pubnub, s"Your de is assigned")
            break()
          }
          Thread.sleep(Math.ceil(cur_to_nxt).toInt * 60 * 1000L)
        }
      }
    }

    pubnub.shutdown()

  }

  private def notifyDriver(pubnub: Pubnub, msg: String) = {
    val callback = new Callback() {
      override def successCallback(channel: String, response: Object): Unit =
        println(response.toString)

      override def errorCallback(channel: String, pubnubError: PubnubError): Unit =
        println(pubnubError.getErrorString)
    }
    pubnub.publish("driverChannel", msg, callback)
    Thread.sleep(200)
  }

  private def notifyUser(pubnub: Pubnub, msg: String) = {

    val callback = new Callback() {
      override def successCallback(channel: String, response: Object): Unit =
        println(response.toString)

      override def errorCallback(channel: String, pubnubError: PubnubError): Unit =
        println(pubnubError.getErrorString)
    }
    pubnub.publish("userChannel", msg, callback)
    Thread.sleep(200)
  }

  private def getTravelTime(lat1: Double, lng1: Double, lat2: Double, lng2: Double): Double = {

    val url = "https://maps.googleapis.com/maps/api/distancematrix/json"

    val response: HttpResponse[String] = Http(url)
      .option(HttpOptions.allowUnsafeSSL)
      //      .param("units", "imperial")
      .param("origins", s"$lat1,$lng1")
      .param("destinations", s"$lat2,$lng2")
      .param("key", "AIzaSyAXoEZxb8sAHKDx-9KcRCfq_U-hEami2bo")
      .asString

    val jsonBody = Json.parse(response.body)

    println("..time.. body:  " + jsonBody)

    Math.ceil((((jsonBody \ "rows" \ 0).get \ "elements" \ 0 \ "duration" \ "value").get.toString
    ().toDouble) / 60.0)
  }
}
