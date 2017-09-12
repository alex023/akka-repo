package main

import java.util.concurrent.TimeUnit

import akka.actor.{ActorSystem, Props}

object Main {

  def main(args: Array[String]): Unit = {
    val system = ActorSystem("ReactiveEnterprise")
    val roomRef = system.actorOf(Props[service.Room], "room")
    roomRef ! "ok"
    TimeUnit.SECONDS.sleep(10)
    system.terminate()
  }

}
