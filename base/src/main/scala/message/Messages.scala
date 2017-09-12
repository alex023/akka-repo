package message

sealed trait ChatMessage

case object Connect extends ChatMessage

case class Request(content: String) extends ChatMessage

case class Response(ok: Boolean) extends ChatMessage
