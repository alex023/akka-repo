package service

import akka.actor.{Actor, ActorRef, Identify, Terminated}
import akka.io.Tcp.Connected
import message.{BroadMsg, Connect, NewMsg}

import scala.collection.mutable

class Room extends Actor {
  val onlineMember = new mutable.HashMap[ActorRef, Boolean]()
  var count = 0

  def receive: Receive = {
    case Identify =>
      println(s"连接请求 Identify $sender()")

    case Connect =>
      onlineMember.put(context.sender(), true)
      context.watch(context.sender())
      context.sender() ! Connected
      println(s"连接请求 Connect $sender()")
    case ms: NewMsg =>
      //接收到消息，并分发给在线玩家
      count += 1
      for (actorRef <- onlineMember.keys) {
        actorRef ! BroadMsg(ms.content)
      }
      println(s"当前在线玩家 ${onlineMember.size}")
    case t:Terminated =>
      onlineMember.remove(t.actor)
    case "getOnlineVisitor" =>
      sender() ! onlineMember.size
    case "getMsgNum" =>
      sender() ! count
    case msg:Any =>
      println(s"receive unkown type $msg")
//      throw new ClassNotFoundException
  }

}
