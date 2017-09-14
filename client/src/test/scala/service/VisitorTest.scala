package service

import akka.actor.{ActorSystem, PoisonPill, Props}
import akka.testkit.{ImplicitSender, TestKit}
import com.typesafe.config.ConfigFactory
import message.{Connect, NewMsg}
import org.scalatest.{BeforeAndAfterAll, FlatSpecLike, Matchers}

class VisitorTest(_system: ActorSystem) extends akka.testkit.TestKit(_system)
  //  class RoomTest(_system: ActorSystem) extends TestKit(_system)
  with ImplicitSender with FlatSpecLike with Matchers with BeforeAndAfterAll {
  override def afterAll(): Unit = {
    TestKit.shutdownActorSystem(system)
  }

  def this() = this(ActorSystem(
    "FaultHandlingDocSpec",
    ConfigFactory.parseString(
      """
      akka {
        loggers = ["akka.testkit.TestEventListener"]
        loglevel = "WARNING"
      }
      """)))

  "A server" must "catch Terminated" in {

    //    val room = expectMsgType[ActorRef] // retrieve answer from TestKit’s testActor
    val clientRef = system.actorOf(Props[Visitor], "user")
    val room = system.actorOf(Props[Room], "room")
    //测试连接
    room tell(Connect, clientRef)
    room ! "getOnlineVisitor"
    expectMsg(1)
    //测试消息收发
    room ! NewMsg("message 1")
    room ! NewMsg("message 2")
    room ! NewMsg("message 3")
    room ! "getMsgNum"
    expectMsg(3)
    java.util.concurrent.TimeUnit.MILLISECONDS.sleep(500)
    clientRef ! "getMsgNum"
    expectMsg(3)
    //测试断线
    clientRef ! PoisonPill //通知客户端断线，由系统告知Room Terminated
    java.util.concurrent.TimeUnit.MILLISECONDS.sleep(500)
    room ! "getOnlineVisitor" //获取Room的在线Visitor情况
    expectMsg(0)

  }
}
