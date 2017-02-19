package petrify.model

import petrify.api

object PetriNet {
  def empty:PetriNet = new PetriNet(Set(), Set())

  def apply(places: Iterable[Place], transitions: Iterable[api.Transition]): PetriNet = {
    val placeSet = places.toSet

    for(transition <- transitions) {
      transition.places subsetOf placeSet
    }

    new PetriNet(placeSet, transitions.toSet)
  }
}

class PetriNet private (override val places: Set[Place], override val transitions: Set[api.Transition])
    extends api.PetriNet
    with Serializable {

  def addPlace(place: Place): PetriNet = if(places contains place) this else {
    new PetriNet(places + place, transitions)
  }

  def addTransition(transition: api.Transition): PetriNet = if(transitions contains transition) this else {
    new PetriNet(places, transitions + transition)
  }

  override def iterate(state: api.State): Iterable[State] = {
    transitions flatMap { transition => transition availableStates state }
  }

  override def iterate(state: api.State, places: api.Place*): api.PetriNet.CollectionObservingState = {
    val placeSet = places.toSet
    val states = transitions flatMap { transition => transition observeAvailableStates (state, placeSet) }
    states.toMap
  }
}
