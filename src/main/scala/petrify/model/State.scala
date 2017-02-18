package petrify.model

import scala.collection.immutable.{TreeMap, SortedMap}

object State {
  def empty:State = new State(new TreeMap())

  def apply(elems: (Place, Int)*):State = new State(SortedMap(elems: _*))

  def create(elems: Iterable[(Place, Int)]):State = State(elems.toSeq :_*)
}

class State private(val placesValues: SortedMap[Place, Int])
  extends PartialFunction[Place, Int]
  with Serializable {

  def places : Iterable[Place] = placesValues.keys

  def values : Iterable[Int] = placesValues.values

  def placesSatisfying(predicate: ((Place, Int)) => Boolean):Set[Place] =
    (placesValues withFilter predicate map (_._1)).toSet

  val activePlaces: Set[Place] = placesSatisfying { case (place, value) =>  value > 0 }
  val freePlaces: Set[Place] = placesSatisfying { case (place, value) =>   value + 1 <= place.max }

  def isActive(places: Set[Place]): Boolean = places subsetOf activePlaces
  def isFree(places: Set[Place]): Boolean = places subsetOf freePlaces

  // TODO test both for achieving min and max values
  def decrementPlace(place: Place): Option[State] = {
    // TODO merge decrement and increment
    val oldValue = placesValues get place match {
      case Some(v) => v
      case None => 0
    }
    val newValue = oldValue - 1

    if(newValue < 0) None else Some(new State(placesValues + (place -> newValue)))
  }

  def incrementPlace(place: Place): Option[State] = {
    val oldValue = placesValues get place match {
      case Some(v) => v
      case None => 0
    }
    val newValue = oldValue + 1

    if(newValue > place.max) None else Some(new State(placesValues + (place -> newValue)))
  }

  private def accumulate(map: (State, Place) => Option[State])(places: Traversable[Place]): Option[State] = {
    val acc = Some(this):Option[State]

    (places foldLeft acc) {
      case (Some(res), place) => map(res, place)
      case (None, _) => None
    }
  }

  def decrementPlaces(places: Traversable[Place]): Option[State] =
    accumulate { case (tempState, place) => tempState.decrementPlace(place) }(places)

  def incrementPlaces(places: Traversable[Place]): Option[State] =
    accumulate { case (tempState, place) => tempState.incrementPlace(place) }(places)

  override def isDefinedAt(x: Place): Boolean = placesValues contains x

  override def apply(v1: Place): Int = placesValues(v1)

  override def toString:String = {
    val namePairs = for ((place, value) <- placesValues) yield s"${place.name}=$value<=${place.max}"
    "State(" + (namePairs mkString ", ") + ")"
  }

  override def hashCode():Int = {
    placesValues.hashCode()
  }

  override def equals(that: Any):Boolean = {
    that match {
      case state: State => this.placesValues equals state.placesValues
      case _ => false
    }
  }
}
