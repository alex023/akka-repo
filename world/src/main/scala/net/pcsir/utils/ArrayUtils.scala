package net.pcsir.utils

import scala.util.Random

/**
  * 数组的辅助工具类
  */
object ArrayUtils {

  /**
    * 将数组元素打乱
    * @param arr 数组
    * @tparam T
    * @return 打乱之后的数组
    */
  def unSort[@specialized(Specializable.AllNumeric) T](
      arr: Array[T]): Array[T] = {
    //val random: SecureRandom = new SecureRandom()
    var index = 0
    val len = arr.length
    while (index < len) {
      val rndIndex = Random.nextInt(len)
      if (index != rndIndex) {
        val tmp = arr(rndIndex)
        arr(rndIndex) = arr(index)
        arr(index) = tmp
      }
      index += 1
    }
    arr
  }
}
