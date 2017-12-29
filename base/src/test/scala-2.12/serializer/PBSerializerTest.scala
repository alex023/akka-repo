package serializer

import message.{HandShakingReq, MsgHeader}
import message.HeaderType.Header_Request
import org.scalatest.WordSpec
import serializer.PBSerializer

/**
  * Created by jiangcheng on {DATE}
  * 功能描述：
  *
  */
class PBSerializerTest extends WordSpec {
  "PBSerializerTest" should {
    val obSerializer = new PBSerializer()
    "fromBinary and toBinary" in {
      val obj = HandShakingReq(Some(MsgHeader(Header_Request, Some(1L))),
                               "345-xfeg",
                               "1234-5678")
      val bytes = obSerializer.toBinary(obj)
      assert(bytes != Array.emptyByteArray)

      val newObj = obSerializer.fromBinary(bytes, obj.getClass.getName)
      assert(obj == newObj)

      val errObj = obSerializer.fromBinary(bytes, "unkown obj name")
      assert(obj != errObj)
    }

  }
}
