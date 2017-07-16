package db


import java.sql.{Connection, DriverManager}

import scala.collection.mutable.ListBuffer

/**
  *
  * Created by Rishabh on 15/07/17.
  */

class DBClient {



  val driver = "com.mysql.jdbc.Driver"
  val url = "jdbc:mysql://localhost/swiggy_hack_jul17?useSSL=false"
  val username = "root"
  val password = "password"
  var connection: Connection = DriverManager.getConnection(url, username, password)


  def createConnection(): Unit = {
    connection = DriverManager.getConnection(url, username, password)
  }

  def closeConnection(): Unit = {
    connection.close()
  }

  def getRestaurantLocation(restaurantId: Int): (Double, Double) = {
    Class.forName(driver)
    val query = s"""select lat, lng from restaurants where id=${restaurantId.toString}"""
    val statement = connection.createStatement()
    val resultSet = statement.executeQuery(query)
    if (resultSet.next()) {
      val lat = resultSet.getDouble("lat")
      val lng = resultSet.getDouble("lng")
      (lat, lng)
    } else {
      (0.0, 0.0)
    }
  }

  def getUserLoc(): (List[(Double, Double)]) = {

    Class.forName(driver)
    val userLat: ListBuffer[(Double, Double)] = ListBuffer.empty[(Double, Double)]

    val query = s"""select ulat, ulng from usertopu order by id"""
    val statement = connection.createStatement()
    val resultSet = statement.executeQuery(query)
    while (resultSet.next()) {
      val lat = resultSet.getDouble("ulat")
      val lng = resultSet.getDouble("ulng")
      userLat += Tuple2(lat, lng)
    }
    userLat.toList
  }
}

object DBClient {
  def main(args: Array[String]): Unit = {

    val dbc = new DBClient
    println(dbc.getRestaurantLocation(69760594))
  }


}

