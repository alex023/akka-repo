package net.pcsir.service.module

/**
  * 全局基础类型、枚举定义
  */
object Define {

  /**
    * 资源等级
    */
  type ResourceLevel = Int

  /**
    *  区块等级
    */
  type GridLevel = Int

  /**
    * 地貌类型定义
    */
  sealed abstract class TypeLandForm
  object Plain extends TypeLandForm
  object Sand extends TypeLandForm
  object Sea extends TypeLandForm
  object Lake extends TypeLandForm
  object Mountain extends TypeLandForm

  /**
    * 地表设施类型
    */
  sealed abstract class TypeFacility
  case object TypeCastle extends TypeFacility
  case object TypeResource extends TypeFacility
}
