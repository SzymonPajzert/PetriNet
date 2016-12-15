package model

object PetriNet {
  def empty = new PetriNet(Map(), Map())

  def apply(places: Iterable[Place], transitions: Iterable[Transition]): PetriNet = {
    val placeSet = places.toSet

    for(transition <- transitions) {
      for(place <- transition.input ++ transition.output) {
        assert(placeSet.contains(place))
        // TODO find nice exception to be thrown
        // TODO find where place exceptions in scala
      }
    }

    new PetriNet(places.map(x => (x.name, x)).toMap, transitions.map(x => (x.name, x)).toMap)
  }
}

class PetriNet private (val places: Map[String, Place], val transitions: Map[String, Transition]) extends Function[State, Iterable[State]] {

  def getPlace(name: String) = places get name

  def addPlace(place: Place): PetriNet = getPlace(place.name) match {
    case None => new PetriNet(places + (place.name -> place), transitions)
    case Some(_) => this
  }

  def getTransition(name: String): Option[Transition] = transitions get name

  def addTransition(transition: Transition): PetriNet = getTransition(transition.name) match {
    case None => new PetriNet(places, transitions + (transition.name -> transition))
    case Some(_) => this
  }

  def activeTransitions(state: State) = transitions.values filter { case x => x.isActive(state) }

  override def apply(state: State): Iterable[State] = {
    activeTransitions(state) flatMap { case transition => transition availableStates state }
  }
}
