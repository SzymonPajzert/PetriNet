package model

case class Place(name:String, max: Int) extends Ordered[Place]{
  override def compare(that: Place): Int = this.name compare that.name match {
    case 0 => this.max compare that.max
    case v => v
  }
}

case class Transition(name:String)(input: Place*)(output: Place*) {
  def isActive(state: State): Boolean = state containsPlaces input

  def availableStates(state: State) : Traversable[State] = {
    state decrementPlaces input match {
      case None => Traversable()
      case Some(decreasedState) => for {
        outputPlace <- output
        increasedState <- decreasedState incrementPlace outputPlace
      } yield increasedState
    }
  }
}

class PetriNet private (val places: Map[String, Place], val transitions: Map[String, Transition]) extends Function[State, Traversable[State]] {

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

  override def apply(state: State): Traversable[State] = {
    activeTransitions(state) flatMap { case transition => transition availableStates state }
  }
}
