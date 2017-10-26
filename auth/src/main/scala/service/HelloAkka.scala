package service

import akka.http.scaladsl.server.{HttpApp, Route}
class HelloAkka extends HttpApp {
  def routes: Route =
    path("hello") {
      get {
        import akka.http.scaladsl.marshallers.xml.ScalaXmlSupport._
        complete(<h1>Say hello to akka-http</h1>)
      }
    } ~
      path("book") {
        get {
          parameter('name.as[Option[String]],
                    'isbn.as[Option[String]],
                    'author.as[Option[String]]) {
            (maybeName, maybeIsbn, maybeAuthor) =>
              complete(
                s"name:${maybeName.getOrElse("")},isbn:$maybeIsbn,author:$maybeAuthor")
          }
        }
      }
}
