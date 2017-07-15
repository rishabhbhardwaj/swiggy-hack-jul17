package service

/**
  *
  * Created by Rishabh on 15/07/17.
  */

import org.eclipse.jetty.server._
import org.eclipse.jetty.servlet.ServletContextHandler
import org.eclipse.jetty.util.ssl.SslContextFactory
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


    val https = new HttpConfiguration()
    https.addCustomizer(new SecureRequestCustomizer())

    val sslContextFactory = new SslContextFactory()
//    sslContextFactory.setKeyStorePath(AppServer.getClass.getResource("keystore.jks").toExternalForm())
    sslContextFactory.setKeyStorePath("/keystore.jks")
    sslContextFactory.setKeyStorePassword("password")
    sslContextFactory.setKeyManagerPassword("password")

    val sslConnector = new ServerConnector(jettyServer,
      new SslConnectionFactory(sslContextFactory, "http/1.1"),
      new HttpConnectionFactory(https))
    sslConnector.setPort(8082)

    val connector = new ServerConnector(jettyServer)
    connector.setPort(8083)

//    val http: ServerConnector = new ServerConnector(jettyServer)
//    http.setPort(8082)
//    http.setIdleTimeout(30000)

    val jerseyServlet = context.addServlet(classOf[ServletContainer], "/*")

    jerseyServlet.setInitOrder(0)
    jerseyServlet.setInitParameter(
      "jersey.config.server.provider.classnames", classOf[Resource].getCanonicalName)

    jettyServer.setConnectors(Array(sslConnector, connector))


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

