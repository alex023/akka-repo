package net.pcsir.service.module

import akka.actor.{Actor, ActorRef}
import net.pcsir.service.module.Define.{
  ResourceLevel,
  TypeCastle,
  TypeFacility,
  TypeResource
}

/**
  * 地面设施高层定义，明确被外部容器调用的接口
  */
trait Facilities {
  val point: Point
  val typ: TypeFacility

  def shouldRefresh: Boolean

  def onMessage(msg: Any): Unit
}

/**
  * 地图资源
  * @param point  资源所在的坐标点
  * @param level  资源的等级，将决定初始化资源数量
  */
class Resource(val point: Point, val level: ResourceLevel) extends Facilities {

  import net.pcsir.service.config.{Resource => Config}

  val typ = TypeResource
  private val initRes = Config.getInit(level)
  private var currRes = initRes

  /**
    * 资源采集
    * @param num  采集的资源数量
    * @return 当前资源实际扣除的资源，通常情况下与输入值一致，但档期那剩余值不足，则为扣除前的剩余值。
    */
  def gather(num: Int): Int = {
    val gatherNum = num.min(currRes)
    currRes -= gatherNum
    gatherNum
  }

  //当前的剩余资源数量
  def current(): Int = currRes

  //当剩余资源小于初始值的10%，允许更新
  override def shouldRefresh: Boolean = currRes / initRes * 100 < 10

  override def onMessage(msg: Any): Unit = {
    //do nothing
  }
}

object Resource {
  val noResource = Resource(Point(-1, -1), -1)

  def apply(point: Point, level: ResourceLevel): Resource =
    new Resource(point, level)
}

/**
  * Description:  玩家在地图上的城堡
  *
  * Created  by:  jiangcheng on 2018/2/28
  */
class Castle(val point: Point) extends Facilities {
  val typ = TypeCastle
  @volatile var onlinePlayer: ActorRef = _

  override def shouldRefresh: Boolean = false

  override def onMessage(msg: Any): Unit = {
    if (onlinePlayer != Nil) onlinePlayer.tell(msg, Actor.noSender)
  }
}
