package petrify.api

trait Transition extends Serializable {
  def places : Set[Place]

  def availableStates(state: State) : Option[State]

  def observeAvailableStates(state: State, places: Set[Place]): Option[PetriNet.ObservingState]
}

class TransitionBuilder 

