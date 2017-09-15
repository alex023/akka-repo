package service

import akka.actor.{ActorSystem, PoisonPill, Props}
import akka.testkit.{ImplicitSender, TestKit}
import com.typesafe.config.ConfigFactory
import message.{Connect, NewMsg}
import org.scalatest.{BeforeAndAfterAll, FlatSpecLike, Matchers}

class VisitorTest(_system: ActorSystem) extends akka.testkit.TestKit(_system)
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

  "A visitor" must "push and receive messages" in {
    val room = system.actorOf(Props[Room], "room")
    val path = "akka://FaultHandlingDocSpec/user/room"
    val clientRef = system.actorOf(Props(classOf[Visitor], path), "visitor")

    java.util.concurrent.TimeUnit.MILLISECONDS.sleep(500)
    //测试连接
    //    room tell(Connect, clientRef)
    room ! "getOnlineVisitor"
    java.util.concurrent.TimeUnit.MILLISECONDS.sleep(200)
    expectMsg(1)

    //测试消息收发,[client]--<NewMsg>--->[Room]---><BroadMsg>--->[client]
    clientRef ! NewMsg("message 1")
    clientRef ! NewMsg("message 2")
    clientRef ! NewMsg("message 3")
    clientRef ! "getSendNum"
    expectMsg(3)
    java.util.concurrent.TimeUnit.MILLISECONDS.sleep(200)
    room ! "getMsgNum"
    expectMsg(3)
    java.util.concurrent.TimeUnit.MILLISECONDS.sleep(200)
    clientRef ! "getReceiveNum"
    expectMsg(3)
    //测试断线
    clientRef ! PoisonPill //通知客户端断线，由系统告知Room Terminated
    java.util.concurrent.TimeUnit.MILLISECONDS.sleep(500)
    room ! "getOnlineVisitor" //获取Room的在线Visitor情况
    expectMsg(0)

  }
}
