package core

/*
 * 生产性建筑的抽象,比如增兵所
 */
trait Productive[T] {
  def produce(): T
}
