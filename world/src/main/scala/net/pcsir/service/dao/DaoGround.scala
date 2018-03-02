package net.pcsir.service.dao

import net.pcsir.service.module.Define.{Lake, TypeLandForm}
import net.pcsir.service.module.{Facilities, Point, Resource}

import scala.collection.mutable.ArrayBuffer
import scala.util.Random

case class EntityLandForm(x: Int, y: Int, landform: TypeLandForm)
case class EntitySurface(x: Int, y: Int, facility: Facilities)

/**
  * Created by jiangcheng on 2018/2/28
  * 功能描述：从数据库中加载地面数据
  * todo:需要通过slick调用数据库
  */
object DaoGround {
  //加载地貌数据
  def loadLandForm(): Array[EntityLandForm] = {
    //todo :从数据库中读取数据
    val tmp = new ArrayBuffer[EntityLandForm]
    for { i <- 0 until 100 } {
      tmp += EntityLandForm(i, i, Lake)
    }
    tmp.toArray
  }

  //加载地表设施
  def loadSurface(): Array[EntitySurface] = {
    //mock
    // todo :从数据库中读取数据
    val tmp = new ArrayBuffer[EntitySurface]
    for { i <- 0 until 100 } {
      tmp += EntitySurface(i,
                           i,
                           new Resource(Point(i, i), Random.nextInt() + 1))
    }
    tmp.toArray
  }
}
