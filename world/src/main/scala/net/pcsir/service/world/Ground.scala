package net.pcsir.service.world

import net.pcsir.service.dao.DaoGround
import net.pcsir.service.module.Define.{TypeFacility, TypeLandForm}
import net.pcsir.service.module.{Facilities, LandForm, Point}

import scala.collection.mutable.ArrayBuffer

/**
  * 世界地图地面容器，封装了地表数据变化过程中的消息通知，从下向上，分为两层：<br>
  * 1.地貌，landform，基于模板生成的底图，不同网格对应平原、沙漠、湖水（海水）、山地等，将可能影响上层数据生成<br>
  * 2.地表，surface，确定每个网格放置的设施，可能是资源、城堡等
  */
object Ground {
  // 地图的范围大小为[0，511】的正方形
  val BOUNDARY_W: Int = 512
  val BOUNDARY_H: Int = 512

  //消息发送的视窗范围
  val BOUNDARY_MSG: Int = 32

  /**
    *  世界地图的地表数据
    */
  private val surface = {
    val tmp = Array.fill[Option[Facilities]](BOUNDARY_W, BOUNDARY_H)(None)
    val databaseData = DaoGround.loadSurface()
    for (entity <- databaseData) {
      tmp(entity.x)(entity.y) = Some(entity.facility)
    }
    tmp
  }

  /**
    * 世界地图的地貌数据
    */
  private val landform: Array[Array[LandForm]] = {
    val tmp = Array.fill[LandForm](BOUNDARY_W, BOUNDARY_H)(LandForm.plain)
    val databaseData = DaoGround.loadLandForm()
    for (entity <- databaseData) {
      tmp(entity.x)(entity.y) = LandForm.plain
    }
    tmp
  }

  /**
    * 在地图上添加设施
    * @param facility 需要加入的设施
    * @return 加入成功，返回true
    */
  def addFacility(facility: Facilities): Boolean = {
    //todo:消息订阅以及周边设施告知
    val x = facility.point.x
    val y = facility.point.y
    this.synchronized {
      surface(x)(y) match {
        case Some(_) => false
        case None =>
          surface(x)(y) = Some(facility)
          true
      }
    }
  }

  /**
    * 删除指定坐标点的设施
    * @param point  坐标点
    * @return 删除成功，返回true。指定坐标点不合法，或不存在设施，则返回false。
    */
  def delFacility(point: Point): Boolean = {
    //todo:消息注销以及周边设施告知
    val x = point.x
    val y = point.y
    this.synchronized {
      surface(x)(y) match {
        case Some(_) =>
          surface(x)(y) = None
          true
        case None =>
          false
      }
    }
  }

  /**
    * 删除指定的建筑物
    * @param facility 要删除的建筑物
    * @return 删除成功，返回true。
    */
  def delFacility(facility: Facilities): Boolean = delFacility(facility.point)

  //坐标点越界判定
  def isOutBoundary(point: Point): Boolean =
    point.x < 0 || point.x >= BOUNDARY_W || point.y < 0 || point.y >= BOUNDARY_H

  /**
    * 某个坐标点发生事件时，需要推送的消息区域
    * @param point  发生事件的坐标点
    * @return 范围的最小坐标点（左上角）和最大坐标点（右下角）
    */
  def rangePointMsg(point: Point): (Point, Point) = {
    (Point((point.x - BOUNDARY_MSG).max(0), (point.y - BOUNDARY_MSG).max(0)),
     Point((point.x + BOUNDARY_MSG).min(BOUNDARY_W),
           (point.y + BOUNDARY_MSG).min(BOUNDARY_H)))
  }

  /**
    * 按照十字坐标方式，在左上角至右下角的范围内，推送消息
    * @param msg  需要推送的消息
    * @param startPoint 视窗范围的左上角
    * @param endPoint 视窗范围的右下角
    */
  private def sendMsg(msg: Any, startPoint: Point, endPoint: Point): Unit = {
    this.synchronized {
      for {
        x <- startPoint.x until endPoint.x;
        y <- startPoint.y until endPoint.y
      } {
        surface(x)(y) match {
          case Some(facility) =>
            facility.onMessage(msg)
          case None =>
          //do nothing
        }
      }
    }
  }

  /**
    * 以发生事件的坐标点为中心，向窗口范围内推送消息
    * @param msg  需要推送的消息
    * @param eventPoint 发生事件的坐标点
    */
  def sendMsg(msg: Any, eventPoint: Point): Unit = {
    if (!isOutBoundary(eventPoint)) {
      val (startPoint, endPoint) = rangePointMsg(eventPoint)
      sendMsg(msg, startPoint, endPoint)
    }
  }

  /**
    * 瞬间移动地面设施
    * @param from 源地址
    * @param to 目标地址
    * @return 可以移动则返回true
    */
  def move(from: Point, to: Point): Boolean = {
    if (isOutBoundary(from) || isOutBoundary(to)) {
      false
    } else {
      //如果坐标在地图范围内，才开始进行目标点空置判定
      this.synchronized {
        surface(to.x)(to.y) match {
          case Some(e) =>
            false
          case None =>
            val facility = surface(from.x)(from.y)
            surface(to.x)(to.y) = facility
            surface(from.x)(from.y) = None
            true
        }
      }
    }
  }

  /**
    * 将指定范围中，找出当前为空能够容纳某类设施的所有地块
    * @param start  指定范围的左上角坐标
    * @param end  指定范围的右下角坐标
    * @param typ  设施类型
    * @return 符合条件的列表
    */
  def vacantPoint(start: Point, end: Point, typ: TypeFacility): Array[Point] = {
    val resultBuf = new ArrayBuffer[Point]()
    this.synchronized {
      for (y <- start.y until end.y; x <- start.x until end.x) {
        if (surface(x)(y).isEmpty && landform(x)(y).canPlace(typ)) {
          resultBuf += Point(x, y)
        }
      }
    }
    resultBuf.toArray
  }
}
