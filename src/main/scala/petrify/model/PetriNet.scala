package petrify.model

object PetriNet {
  def empty:PetriNet = new PetriNet(Set(), Set())

  def apply(places: Iterable[Place], transitions: Iterable[Transition]): PetriNet = {
    val placeSet = places.toSet

    for(transition <- transitions) {
      for(place <- transition.input ++ transition.output) {
        assert(placeSet.contains(place))
      }
    }

    new PetriNet(placeSet, transitions.toSet)
  }
}

class PetriNet private (override val places: Set[Place], override val transitions: Set[Transition])
    extends PetriNetAPI
    with Serializable {

  def addPlace(place: Place): PetriNet = if(places contains place) this else {
    new PetriNet(places + place, transitions)
  }

  def addTransition(transition: Transition): PetriNet = if(transitions contains transition) this else {
    new PetriNet(places, transitions + transition)
  }

  def activeTransitions(state: State):Iterable[Transition] = transitions filter { x => x.isActive(state) }

  override def iterate(state: State): Iterable[State] = {
    activeTransitions(state) flatMap { transition => transition availableStates state }
  }

  override def iterate(state: State, places: Place*): PetriNetAPI.CollectionObservingState = {
    val placeSet = places.toSet
    val states = activeTransitions(state) flatMap { transition => transition observeAvailableStates (state, placeSet) }
    states.toMap
  }
}
