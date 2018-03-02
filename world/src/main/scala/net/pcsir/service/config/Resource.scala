package net.pcsir.service.config

import net.pcsir.service.module.Define.ResourceLevel

/**
  * Created by jiangcheng on 2018/2/28
  * 功能描述：资源的等级配置
  *
  */
object Resource {
  private lazy val cache: Map[ResourceLevel, Int] = {
    //mock,should todo
    var map = Map.empty[ResourceLevel, Int]
    for (i <- 1 to 10) {
      map += (i -> i * 500)
    }
    map
  }

  def getInit(level: ResourceLevel): Int = cache.getOrElse(level, 0)
}
