package petrify.snoopy.parser
import petrify.model.{PetriNet, Place, State, Transition}
import petrify.snoopy.model.{Node, Edge => ParEdge, Place => ParPlace, Transition => ParTransition, _} // TODO save it as a good knowledge

// TODO save as knowledge: sbt test:console

import scala.collection.immutable.HashMap

case class ParseResult(net: PetriNet, state: State) {}

// TODO remove this redundancy of edge type
sealed trait DirectedEdgeType
final case object InputEdge extends DirectedEdgeType
final case object OutputEdge extends DirectedEdgeType
final case object DirectedReadEdge extends DirectedEdgeType
final case object DirectedInhibitorEdge extends DirectedEdgeType

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

  lazy val places = (parsedPlaces map { case ParPlace(id, name, marking, capacity) => (id, (Place(name, capacity), marking)) }).toMap
  lazy val transitions = (parsedTransitions map { case ParTransition(id, name) => (id, Transition(name)) }).toMap

  def updateTransition(transitions: Transitions, edge: ParEdge):Transitions = {
    val ParEdge(edgeType, _, source, target, _) = edge

    val (placeId, transitionId, directedEdgeType:DirectedEdgeType) = edgeType match {
      case NormalEdge => {
        if(places contains source) (source, target, InputEdge) else (target, source, OutputEdge)
      }
      case ReadEdge => (source, target, DirectedReadEdge)
      case InhibitorEdge => (source, target, DirectedInhibitorEdge)
    }

    val place = places(placeId)._1
    val transition = transitions(transitionId)

    val newTransition = directedEdgeType match {
      case InputEdge => transition addInput place
      case OutputEdge => transition addOutput place
      case DirectedReadEdge => transition addRead place
      case DirectedInhibitorEdge => transition addInhibitor place
    }

    transitions + (transitionId -> newTransition)
  }

  def build:(PetriNet, State) = {

    val newTransitions = (edges foldLeft transitions) { updateTransition(_, _) }
    // TODO Parsing states - remove this one
    (PetriNet(places.values map (_._1), newTransitions.values), State.create(places.values))
  }
}
