package service_client


import akka.actor.Actor
import message.{Request, Response}

class Visitor extends Actor {
  private var count = 0

  def Count(): Int = {
    count += 1
    count
  }

  override def receive: Receive = {
    case Response(ok) => println("user receive response:", ok)
    case _ => println("user receive server:", Count)
  }
}
