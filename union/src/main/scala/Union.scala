package union

import akka.actor.{ActorSystem, Props}
import actor.Clan

object Union {

  def main(args: Array[String]): Unit = {
    startRemoteCalculatorSystem()
  }

  def startRemoteCalculatorSystem(): Unit = {
    val system = ActorSystem("ChatServer")
    system.actorOf(Props[Clan], "room")

    println("Started ChatServer - waiting for messages")

  }

}
