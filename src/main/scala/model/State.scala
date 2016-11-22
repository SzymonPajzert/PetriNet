package model

import scala.collection.immutable.HashMap

object State {
  def empty = new State(new HashMap())
}

class State private(private val state: Map[Place, Int]) extends PartialFunction[Place, Int] {

  def containsPlaces(subPlaces: Traversable[Place]): Boolean =
    (subPlaces foldLeft true){ case (prev, cur) => prev && (state contains cur) }

  def decrementPlace(place: Place, value: Int = 1): State = {
    val oldValue = state get place match {
      case Some(v) => v
      case None => 0
    }
    val newValue = oldValue - value
    if(newValue < 0) throw new IllegalArgumentException
    new State(if (newValue > 0) state + (place -> newValue) else state - place)
  }

  def incrementPlace(place: Place, value: Int = 1): State = {
    state get place match {
      case Some(n) => new State(state + (place -> (n + value)))
      case None => new State(state + (place -> value))
    }
  }

  override def isDefinedAt(x: Place): Boolean = state contains x

  override def apply(v1: Place): Int = state(v1)
}
