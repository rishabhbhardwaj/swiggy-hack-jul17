package service


import play.api.libs.json.Json

import scalaj.http.{Http, HttpOptions, HttpResponse}


object Test {
  def main(args: Array[String]): Unit = {


    val url2 ="https://maps.googleapis.com/maps/api/distancematrix/json"

    val t1 = System.currentTimeMillis()
    println("abcd" )
    val response: HttpResponse[String] = Http(url2)
//        .option(HttpOptions.allowUnsafeSSL)
//        .param("units", "imperial")
        .param("origins", "40.6655101,-73.89188969999998")
        .param("destinations", "40.6655101,-73.89188969999998")
        .param("key", "AIzaSyAXoEZxb8sAHKDx-9KcRCfq_U-hEami2bo")
        .asString
    println(System.currentTimeMillis() - t1)

    val jsonBody = Json.parse(response.body)

    println(System.currentTimeMillis() - t1)

    println(((jsonBody \ "rows" \ 0).get \ "elements" \ 0 \ "duration" \ "text").get.toString().split(" " +
      "")(0).replaceAll("\"","").toDouble)


  }


}
