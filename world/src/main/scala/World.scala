import akka.actor.{ActorSystem, Props}
import actor.World

object World {
  def main(args: Array[String]): Unit = {}
  def startWorldServer(): Unit = {
    println("Started Connect Server")
    val system = ActorSystem("world")
    val actor = system.actorOf(Props[World])

  }
}
