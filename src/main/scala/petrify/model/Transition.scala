package petrify.model

import petrify.api

object Transition {
  def apply(name: String):Transition = new Transition(name, Set(), Set(), Set(), Set())

  def apply(input: Place*)(name: String)(output: Place*): Transition = Transition(name, input.toSet, output.toSet, Set(), Set())

  implicit class TransitionBuilder(transition: Transition) {
    def addInput(additional: Place*): Transition = transition.copy(input = transition.input ++ additional)

    def addOutput(additional: Place*): Transition = transition.copy(output = transition.output ++ additional)

    def addRead(additional: Place*): Transition = transition.copy(read = transition.read ++ additional)

    def addInhibitor(additional: Place*): Transition = transition.copy(inhibitor = transition.inhibitor ++ additional)
  }
}

final case class Transition (
  name:String,
  input: Set[Place],
  output: Set[Place],
  read: Set[Place],
  inhibitor: Set[Place]
) extends api.Transition {

  // TODO Check in tests
  def places: Set[Place] = input ++ output ++ read ++ inhibitor

  def availableStates(state: State) : Option[State] = {
    if((state isActive read) && (state isAbsent inhibitor)) {
      for {
        decreasedState <- state decrementPlaces input
        if decreasedState isFree output
        result <- decreasedState incrementPlaces output
      } yield result
    } else {
      None
    }
  }

  def observeAvailableStates(state: State, places: Set[Place]): Option[api.PetriNet.ObservingState] = {
    def createMap[K, V](keys: Iterable[K], value: V): Map[K, V] = {
      (for(key <- keys) yield key -> value).toMap
    }

    val (usedPlaces, unusedPlaces) = places partition { place => input contains place }
    val resultMap = createMap(usedPlaces, true) ++ createMap(unusedPlaces, false)

    availableStates(state) map { state => (state, resultMap)}
  }

  override def toString:String = s"Transition($name)"
}
