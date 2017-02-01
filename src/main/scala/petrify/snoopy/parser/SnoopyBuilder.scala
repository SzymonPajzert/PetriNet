package petrify.snoopy.parser
import petrify.model.{PetriNet, Place, State, Transition}
import petrify.snoopy.model.{Node, Edge => ParEdge, Place => ParPlace, Transition => ParTransition}

import scala.collection.immutable.HashMap

object NoSuchTransition extends NoSuchElementException
object NoSuchPlace extends NoSuchElementException

object SnoopyBuilder {
  def apply(parsedPlaces: Iterable[ParPlace],
            parsedTransitions: Iterable[ParTransition],
            edges: Iterable[ParEdge]): SnoopyBuilder = new SnoopyBuilder(parsedPlaces, parsedTransitions, edges)
}

class SnoopyBuilder(parsedPlaces: => Iterable[ParPlace],
                    parsedTransitions: => Iterable[ParTransition],
                    edges: => Iterable[ParEdge]) {

  type Transitions = Map[Int, Transition]
  type Places = Map[Int, Place]

  lazy val places = (parsedPlaces map { case ParPlace(id, name, capacity) => (id, Place(name, capacity)) }).toMap
  lazy val transitions = (parsedTransitions map { case ParTransition(id, name) => (id, Transition(name)) }).toMap

  def updateTransition(transitions: Transitions, edge: ParEdge):Transitions = {
    val ParEdge(_, _, source, target, _) = edge
    val (placeId, transitionId, toTransition) =
      if(places contains source) (source, target, true) else (target, source, false)

    val place = places.getOrElse(placeId, throw NoSuchPlace)
    val transition:Transition = transitions getOrElse (transitionId, throw NoSuchTransition)

    val newTransition = if (toTransition) transition addInput place else transition addOutput place
    transitions + (transitionId -> newTransition)
  }

  def build:(PetriNet, State) = {

    val newTransitions = (edges foldLeft transitions) { updateTransition(_, _) }
    (PetriNet(places.values, newTransitions.values), State.empty)
  }
}
