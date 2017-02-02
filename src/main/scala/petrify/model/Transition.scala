package petrify.model

object Transition {
  def apply(name: String):Transition = new Transition(name, Set(), Set())

  def apply(input: Place*)(name: String)(output: Place*): Transition = new Transition(name, input.toSet, output.toSet)

  def unapply(arg: Transition): Option[String] = Some(arg.name)
}

class Transition private (val name:String, val input: Set[Place], val output: Set[Place]) extends Serializable {
  def addInput(additionalInput: Place*):Transition = new Transition(name, input ++ additionalInput, output)

  def addOutput(additionalOutput: Place*):Transition = new Transition(name, input, output ++ additionalOutput)

  def isActive(state: State): Boolean = (state isActive input) && (state isFree output)

  def availableStates(state: State) : Option[State] = {
    (state decrementPlaces input) flatMap (decreasedState => decreasedState incrementPlaces output)
  }

  def observeAvailableStates(state: State, places: Set[Place]): Option[PetriNetAPI.ObservingState] = {
    def createMap[K, V](keys: Iterable[K], value: V): Map[K, V] = {
      (for(key <- keys) yield key -> value).toMap
    }

    val (usedPlaces, unusedPlaces) = places partition { place => input contains place }
    val resultMap = createMap(usedPlaces, true) ++ createMap(unusedPlaces, false)

    availableStates(state) map { state => (state, resultMap)}
  }

  override def toString:String = s"Transition($name)"
}
