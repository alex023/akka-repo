package service

import akka.actor.Actor
import akka.io.Tcp.Connected
import message.NewMsg

class Visitor extends Actor {
  private var count = 0


  override def receive: Receive = {
    case Connected => println("user connected")
    case NewMsg(content) =>
      count += 1
      println("user receive msg:", content)
    case "getMsgNum" => sender ! count
  }
}
