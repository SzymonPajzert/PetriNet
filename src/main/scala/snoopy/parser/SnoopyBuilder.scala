package snoopy.parser
import model.{PetriNet, Place, State, Transition}
import snoopy.model.{Node, Edge => ParEdge, Place => ParPlace, Transition => ParTransition}

import scala.collection.immutable.HashMap

object NoSuchTransition extends NoSuchElementException
object NoSuchPlace extends NoSuchElementException

object SnoopyBuilder {

  type Transitions = Map[Int, Transition]
  type Places = Map[Int, Place]

  def build(parsedPlaces: => Iterable[ParPlace],
            parsedTransitions: => Iterable[ParTransition],
            edges: => Iterable[ParEdge]):(PetriNet, State) = {

    val places = (parsedPlaces map { case ParPlace(id, name, capacity) => (id, Place(name, capacity)) }).toMap
    var transitions = (parsedTransitions map { case ParTransition(id, name) => (id, Transition(name)) }).toMap

    for (ParEdge(_, _, source, target, _) <- edges) {
      val (placeId, transitionId, toTransition) =
        if(places contains source) (source, target, true) else (target, source, false)

      val place = places.getOrElse(placeId, throw NoSuchPlace)

      val transition:Transition = transitions getOrElse (transitionId, throw NoSuchTransition)
      val newTransition = if (toTransition) transition addInput place else transition addOutput place
      transitions += ((transitionId, newTransition))
    }
    (PetriNet(places.values, transitions.values), State.empty)
  }
}
