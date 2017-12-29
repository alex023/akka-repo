package serializer

import org.msgpack.annotation.Message
import org.scalatest.WordSpec
import org.msgpack.ScalaMessagePack

class PcsirMsgPackSerializerTest extends WordSpec {
  @Message
  class Person {
    var name = "Jiangcheng"
    var age = 40
  }
  "PcsirMsgPackSerializerTest" should {
    "序列化与反序列化" in {
      val serializer = new MsgPackSerializer()
      val original: Person = new Person()
      val binary: Array[Byte] = serializer.toBinary(original)
      // Turn it back into an object
      val back = serializer.fromBinary(binary)

      // Voilá!
    }
  }
}
