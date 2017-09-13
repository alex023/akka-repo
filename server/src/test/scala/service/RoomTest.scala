package service

import java.util.Date

import akka.actor.{ActorRef, ActorSystem, PoisonPill, Props}
import akka.testkit.{ImplicitSender, TestKit}
import com.typesafe.config.ConfigFactory
import message.{Connect, NewMsg}
import org.scalatest.{BeforeAndAfterAll, FlatSpecLike, Matchers}

class RoomTest(_system: ActorSystem) extends TestKit(_system)
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

  "A supervisor" must "apply the chosen strategy for its child" in {
    val supervisor = system.actorOf(Props[Supervisor], "supervisor")

    supervisor ! Props[Room]
    val room = expectMsgType[ActorRef] // retrieve answer from TestKit’s testActor
    val clientRef = system.actorOf(Props[Visitor], "user")

    //测试连接
    room tell(Connect, clientRef)
    room ! "getOnlineVisitor"
    expectMsg(1)
    //测试消息收发
    room tell(NewMsg("message 1"), clientRef)
    room tell(NewMsg("message 2"), clientRef)
    room tell(NewMsg("message 3"), clientRef)
    room ! "getMsgNum"
    expectMsg(3)

    clientRef ! "getMsgNum"
    expectMsg(3)
    //测试断线
    clientRef ! PoisonPill //通知客户端断线，由系统告知Room Terminated
    println(s"等待开始 ${new Date().getTime}")
    java.util.concurrent.TimeUnit.MICROSECONDS.sleep(10000)
    room ! "getOnlineVisitor" //获取Room的在线Visitor情况
    expectMsg(0)
    println(s"测试完成！${new Date().getTime}")
  }
}
