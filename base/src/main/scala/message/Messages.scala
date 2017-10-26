package message

import akka.actor.ActorRef

sealed trait ClientState

case object Connect extends ClientState

final case class Connected(actor: ActorRef) extends ClientState

case object Disconnect extends ClientState

final case class NewMsg(content: String) extends ClientState

final case class BroadMsg(content: String) extends ClientState
