package serializer

/**
  * Created by jiangcheng on {DATE}
  * 功能描述：
  *
  */
package serializer

import akka.serialization.SerializerWithStringManifest
import com.trueaccord.scalapb.{GeneratedMessage, GeneratedMessageCompanion}
import message.{LogicProto, PayProto, UnionProto}

// Package 是一个网络传输包，包括Header和Body两部分。具体定义如下：
//     --------------------------------------------------------------------------------------
//     |                   ...Header...             |              ...Body...          |
//     --------------------------------------------------------------------------------------
//     |  [0]    |  [1]   |   [2]  |   [3]  |  [4]  |  [5]  |  [6]  |   [7]   |  ...   |    字节序号
//     --------------------------------------------------------------------------------------
//     |             BodyLen                | Sign  |                Message           |    数据名称
//     --------------------------------------------------------------------------------------
//	[0]--[3]	Message数据长度,一次最多传递1<<32的数据长度,也就是4G以内，实际完全用不了这么大。
// 	[4]		标志位:
//			第1位，		即0000-000x中的‘x’表示：压缩标识位，1表示压缩，0表示未压缩。
//			其余位，		保留
//	[5]--[.]	业务数据:		具体的数据内容，其格式遵循Message struct的定义，传输模块不做任何具体处理。
object PBSerializer {
  val register: Map[String, GeneratedMessageCompanion[_]] = {
    var tmp: Map[String, GeneratedMessageCompanion[_]] = Map.empty
    val mapUpdated = (source: Seq[GeneratedMessageCompanion[_]]) => {
      source.foreach(item =>
        tmp = tmp.updated(item.scalaDescriptor.fullName, item))
    }
    mapUpdated(LogicProto.messagesCompanions)
    mapUpdated(UnionProto.messagesCompanions)
    mapUpdated(PayProto.messagesCompanions)

    tmp
  }
  val MSG_COMPRESS_VALVE = 2400 //数据长度压缩的阀值。数据长度［0，2400］不压缩；长度（2400，...）要压缩
  val MSG_MAX_LENGTH = 1 << 20 //数据包最大长度限制（1M），避免利用最大长度（1<<32））拖垮服务器
  val signCompress = 0x01
}

class PBSerializer extends SerializerWithStringManifest {

  import PBSerializer._

  def identifier = 20171101

  // "toBinary" serializes the given object to an Array of Bytes
  def toBinary(obj: AnyRef): Array[Byte] = {
    // 将对象封装到Envelop，库不允许case
    val name = obj.getClass.getName
    obj match {
      case e: GeneratedMessage =>
        e.toByteArray
      case _ => Array.emptyByteArray
    }
  }

  // "fromBinary" deserializes the given array,
  // using the type hint (if any, see "includeManifest" above)
  def fromBinary(bytes: Array[Byte], manifest: String): AnyRef = {
    register.get(manifest) match {
      case Some(companion) =>
        companion.parseFrom(bytes).asInstanceOf[AnyRef]
      case _ => None
    }
  }

  override def manifest(o: AnyRef): String = ""
}
