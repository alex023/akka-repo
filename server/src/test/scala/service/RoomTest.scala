package service

import akka.actor.{ActorRef, ActorSystem, Props}
import akka.testkit.{ImplicitSender, TestKit}
import com.typesafe.config.ConfigFactory
import org.scalatest.{BeforeAndAfterAll, FlatSpecLike, Matchers}
import service_client.Visitor

class FaultHandlingDocSpec(_system: ActorSystem) extends TestKit(_system)
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
    val client=system.actorOf(Props[Visitor],"user")
    room ! new message.Connect
    room ! "get"
    expectMsg(1)
  }
}
