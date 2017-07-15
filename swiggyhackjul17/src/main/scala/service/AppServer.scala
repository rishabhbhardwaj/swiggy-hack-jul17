package service

/**
  *
  * Created by Rishabh on 15/07/17.
  */

import org.eclipse.jetty.server._
import org.eclipse.jetty.servlet.ServletContextHandler
import org.eclipse.jetty.util.thread.QueuedThreadPool
import org.glassfish.jersey.servlet.ServletContainer

object AppServer {

  def main(args: Array[String]): Unit = {

    val context = new ServletContextHandler(ServletContextHandler.SESSIONS)
    context.setContextPath("/")

    val threadPool: QueuedThreadPool = new QueuedThreadPool()
    threadPool.setMaxThreads(50)
    threadPool.setDetailedDump(false)

    val jettyServer = new Server(threadPool)
    jettyServer.setHandler(context)

    val http: ServerConnector = new ServerConnector(jettyServer)
    http.setPort(8082)
    http.setIdleTimeout(30000)

    val jerseyServlet = context.addServlet(classOf[ServletContainer], "/*")

    jerseyServlet.setInitOrder(0)
    jerseyServlet.setInitParameter(
      "jersey.config.server.provider.classnames", classOf[Resource].getCanonicalName)

    jettyServer.setConnectors(Array(http))


    try {
      // Start jetty server

      println("Starting server on port " + jettyServer.getConnectors.head
        .asInstanceOf[ServerConnector].getPort)
      jettyServer.start()
      jettyServer.join()
    } finally {
      jettyServer.destroy()
    }
  }
}

