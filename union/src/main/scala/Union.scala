package union

import akka.actor.{ActorSystem, Props}
import service.Room

object Union {

  def main(args: Array[String]): Unit = {
    startRemoteCalculatorSystem()
  }

  def startRemoteCalculatorSystem(): Unit = {
    val system = ActorSystem("ChatServer")
    system.actorOf(Props[Room], "room")

    println("Started ChatServer - waiting for messages")

  }

}
