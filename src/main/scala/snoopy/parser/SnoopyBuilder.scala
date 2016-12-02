package snoopy.parser
import model.{PetriNet, State}
import snoopy.model._

object SnoopyBuilder {
  def build(places: => Iterable[Place],
            transitions: => Iterable[Transition],
            edges: => Iterable[Edge]):(PetriNet, State) = {
    ???
  }
}
