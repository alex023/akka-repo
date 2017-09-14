package message

sealed trait ChatMessage

final case object Connect extends ChatMessage

final case object Connected extends ChatMessage

final case class NewMsg(content: String) extends ChatMessage

final case class BroadMsg(content: String) extends ChatMessage
