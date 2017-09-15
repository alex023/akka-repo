package message

import akka.actor.ActorRef

sealed trait ChatMessage

case object Connect extends ChatMessage

final case class Connected(actor: ActorRef) extends ChatMessage

final case class NewMsg(content: String) extends ChatMessage

final case class BroadMsg(content: String) extends ChatMessage
