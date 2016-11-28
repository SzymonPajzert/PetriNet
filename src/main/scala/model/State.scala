package model

import scala.collection.immutable.{TreeMap, SortedMap}

object State {
  def empty = new State(new TreeMap())

  def apply(elems: (Place, Int)*) = new State(SortedMap(elems: _*))
}


/* TODO
 *
 * Instantiazation of Travesable() for Traversable[State] should create collection supporting const append
 * It can be done with override of class GenericCompanion[+CC[X] <: GenTraversable[X]] or
 * newBuilder[A].result()
 */

class State private(val placesValues: SortedMap[Place, Int]) extends PartialFunction[Place, Int] {

  def places : Iterable[Place] = placesValues.keys

  def values : Iterable[Int] = placesValues.values

  def containsPlaces(subPlaces: Traversable[Place]): Boolean =
    (subPlaces foldLeft true){ case (prev, cur) => prev && (placesValues contains cur) }

  def decrementPlace(place: Place, value: Int = 1): Option[State] = {
    val oldValue = placesValues get place match {
      case Some(v) => v
      case None => 0
    }
    val newValue = oldValue - value

    if(newValue < 0) None else
      Some(new State(if (newValue > 0) placesValues + (place -> newValue) else placesValues - place))
  }

  def incrementPlace(place: Place, value: Int = 1): Option[State] = {
    val newState = placesValues get place match {
      case Some(n) => new State(placesValues + (place -> (n + value)))
      case None => new State(placesValues + (place -> value))
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

  override def isDefinedAt(x: Place): Boolean = placesValues contains x

  override def apply(v1: Place): Int = placesValues(v1)

  override def toString = {
    val namePairs = for ((place, value) <- placesValues) yield s"${place.name}=$value"
    "State(" + (namePairs mkString ", ") + ")"
  }
}
