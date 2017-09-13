package message

sealed trait ChatMessage

case object Connect extends ChatMessage

case object Connected extends ChatMessage

case class NewMsg(content: String) extends ChatMessage

