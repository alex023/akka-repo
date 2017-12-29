import actor.World
import akka.actor.{ActorSystem, Props}

import scala.collection.mutable

object World {
  def main(args: Array[String]): Unit = {
    xx
  }

  def startWorldServer(): Unit = {
    println("Started Connect Server")
    val system = ActorSystem("world")
    val actor = system.actorOf(Props[World])

  }

  def xx(): Unit = {
    val src = Array(("a", 1), ("a", 2), ("b", 1), ("c", 1), ("c", 2))

    val container = (Map.empty[String, mutable.ArrayBuffer[Int]] /: src) {
      case (m, (key, value)) =>
        val buf: mutable.ArrayBuffer[Int] =
          m.getOrElse(key, mutable.ArrayBuffer.empty[Int])
        buf += value
        m.updated(key, buf)
    }

    for ((key, value) <- container) {
      println(s"$key ->$value")
    }
  }
}
