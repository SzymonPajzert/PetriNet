package model

case class Place(name:String)

case class Transition(name:String, input: List[Place], output: List[Place]) {
  def isActive(state: State): Boolean = state containsPlaces input
}



class PetriNet private (val places: Map[String, Place], val transitions: Map[String, Transition]) extends Function[State, Appendable[State]] {

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

  override def apply(v1: List[Int]): Traversable[List[Int]] = {

  }
}
