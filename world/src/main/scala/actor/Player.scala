package actor

import akka.actor.{Actor, ActorRef}
import message.{Offline, Online}

/*
Player 是玩家在服务端的业务代理，维护对基地、部队的控制，保持与联盟服务的连接。
并在玩家在线期间，cache 客户端发送的信息。
 */
class Player(val account: String) extends Actor {
  //前端代理，当 agent 不为空时，表示用户在线
  var agent: Option[ActorRef] = None
  //部队引用
  var army = Map.empty[String, Set[ActorRef]]
  //城市引用
  var city: Option[ActorRef] = None

  override def preStart(): Unit = {
    //todo:loadfrom db
    //todo:create city and army
  }

  def offline: Receive = {
    case Online =>
      context.watch(sender())
      agent = Some(sender())
      context.become(receive)
    case _ =>
      println("TODO")
  }

  override def receive: Receive = {
    case Offline =>
      agent = null
      context.become(offline)
    case _ =>
      println("TODO")
  }
}
