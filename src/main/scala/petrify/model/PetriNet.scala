package petrify.model

object PetriNet {
  def empty:PetriNet = new PetriNet(Map(), Map())

  def apply(places: Iterable[Place], transitions: Iterable[Transition]): PetriNet = {
    val placeSet = places.toSet

    for(transition <- transitions) {
      for(place <- transition.input ++ transition.output) {
        assert(placeSet.contains(place))
      }
    }

    new PetriNet(places.map(x => (x.name, x)).toMap, transitions.map(x => (x.name, x)).toMap)
  }
}

class PetriNet private (val places: Map[String, Place], val transitions: Map[String, Transition])
  extends PetriNetAPI
  with Serializable {

  def getPlace(name: String):Option[Place] = places get name

  def addPlace(place: Place): PetriNet = getPlace(place.name) match {
    case None => new PetriNet(places + (place.name -> place), transitions)
    case Some(_) => this
  }

  def getTransition(name: String): Option[Transition] = transitions get name

  def addTransition(transition: Transition): PetriNet = getTransition(transition.name) match {
    case None => new PetriNet(places, transitions + (transition.name -> transition))
    case Some(_) => this
  }

  def activeTransitions(state: State):Iterable[Transition] = transitions.values filter { x => x.isActive(state) }

  override def iterate(state: State): Iterable[State] = {
    activeTransitions(state) flatMap { transition => transition availableStates state }
  }

  override def iterate(state: State, places: Place*): PetriNetAPI.CollectionObservingState = {
    val placeSet = places.toSet
    val states = activeTransitions(state) flatMap { transition => transition observeAvailableStates (state, placeSet) }
    states.toMap
  }
}
