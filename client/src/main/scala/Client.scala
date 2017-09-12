import akka.actor.{ActorSystem, Props}
import message.Response

object Client {
  def main(args: Array[String]): Unit = {
    val system=ActorSystem("client")
    val userRef=system.actorOf(Props[service.User],"visitor")


    userRef ! "test"
    userRef ! new Response(true)
    println("client to do")

  }
}
