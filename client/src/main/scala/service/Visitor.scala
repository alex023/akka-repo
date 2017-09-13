package service_client


import akka.actor.Actor
import message.{NewMsg}

class Visitor extends Actor {
  private var count = 0


  override def receive: Receive = {
    case NewMsg(ok) =>
      count += 1
      println("user receive response:", ok)
    case _ => println("user receive server:", count)
  }
}
