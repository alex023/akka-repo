package net.pcsir.service.module

import net.pcsir.service.module.Define._

/**
  * Created by jiangcheng on 2018/3/1
  * 功能描述：
  *
  */
class LandForm(val typeLandForm: TypeLandForm) {

  /**
    * 根据规则库，判断某类型的设施能否放置在该地貌上
    * @param typeFacility 设施类型
    * @return
    */
  def canPlace(typeFacility: TypeFacility): Boolean =
    LandForm.rule(typeLandForm).contains(typeFacility)
}
object LandForm {
  val plain = new LandForm(Plain)
  val sand = new LandForm(Sand)

  private val rule: Map[TypeLandForm, Set[TypeFacility]] = Map(
    (Plain, Set(TypeResource, TypeCastle)))

}
