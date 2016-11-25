package model

import scala.collection.immutable.HashMap

object State {
  def empty = new State(new HashMap())

  def identifiers(ids: Traversable[String]) = {
    val pairs: Traversable[(Place, Int)] = for(id <- ids) yield (Place(id), 0)
    new State(HashMap(pairs))
  }
}

class State private(private val state: Map[Place, Int]) extends PartialFunction[Place, Int] {

  def containsPlaces(subPlaces: Traversable[Place]): Boolean =
    (subPlaces foldLeft true){ case (prev, cur) => prev && (state contains cur) }

  def decrementPlace(place: Place, value: Int = 1): Option[State] = {
    val oldValue = state get place match {
      case Some(v) => v
      case None => 0
    }
    val newValue = oldValue - value

    if(newValue < 0) None else
      Some(new State(if (newValue > 0) state + (place -> newValue) else state - place))
  }

  def incrementPlace(place: Place, value: Int = 1): Option[State] = {
    val newState = state get place match {
      case Some(n) => new State(state + (place -> (n + value)))
      case None => new State(state + (place -> value))
    }
    Some(newState)
  }

  private def accumulate(map: (State, Place) => Option[State])(places: Traversable[Place]): Option[State] = {
    val acc = Some(this):Option[State]

    (places foldLeft acc) {
      case (Some(res), place) => map(res, place)
      case (None, _) => None
    }
  }

  def decrementPlaces(places: Traversable[Place], value: Int = 1): Option[State] =
    accumulate { case (tempState, place) => tempState.decrementPlace(place, value) }(places)

  def incrementPlaces(places: Traversable[Place], value: Int = 1): Option[State] =
    accumulate { case (tempState, place) => tempState.incrementPlace(place, value) }(places)

  override def isDefinedAt(x: Place): Boolean = state contains x

  override def apply(v1: Place): Int = state(v1)

  override def toString = {
    val namePairs = for ((place, value) <- state) yield s"${place.name}=$value"
    "State(" + (namePairs mkString ", ") + ")"
  }
}
