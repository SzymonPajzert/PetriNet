package model

case class Place(name:String)

/* TODO
 *
 * Instantiazation of Travesable() for Traversable[State] should create collection supporting const append
 * It can be done with override of class GenericCompanion[+CC[X] <: GenTraversable[X]] or
 * newBuilder[A].result()
 */

case class Transition(name:String, input: List[Place], output: List[Place]) {
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

  def state(name: String): Option[Place] = places get name

  def addState(name: String): PetriNet = state(name) match {
    case None => new PetriNet(places + (name -> Place(name)), transitions)
    case Some(_) => this
  }

  def transition(name: String): Option[Transition] = transitions get name

  def addTransition(name: String, input: List[Place], output: List[Place]): PetriNet = transition(name) match {
    case None => new PetriNet(places, transitions + (name -> Transition(name, input, output)))
    case Some(_) => this
  }

  def activeTransitions(state: State) = transitions.values filter { case x => x.isActive(state) }

  override def apply(state: State): Traversable[State] = {
    activeTransitions(state) flatMap { case transition => transition availableStates state }
  }
}
