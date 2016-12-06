package model

case class Place(name:String, max: Int) extends Ordered[Place]{
  override def compare(that: Place): Int = this.name compare that.name match {
    case 0 => this.max compare that.max
    case v => v
  }
}

object Transition {
  def apply(name: String) = new Transition(name, Iterable(), Iterable())

  def create(name: String)(input: Place*)(output: Place*): Transition = new Transition(name, input, output)

  def unapply(arg: Transition): Option[String] = Some(arg.name)
}

// TODO move to separate file
class Transition private (val name:String, val input: Iterable[Place], val output: Iterable[Place]) {
  def addInput(additionalInput: Place*):Transition = new Transition(name, input ++ additionalInput, output)

  def addOutput(additionalOutput: Place*):Transition = new Transition(name, input, output ++ additionalOutput)

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

object PetriNet {
  def empty = new PetriNet(Map(), Map())

  def apply(places: Iterable[Place], transition: Iterable[Transition]): PetriNet = {
    ???
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
