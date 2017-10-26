import service.HelloAkka

import scala.io.StdIn

object main {
  def main(args: Array[String]): Unit = {
    val server = new HelloAkka
    server.startServer("localhost", 8080)

//    StdIn.readLine()
  }
}
