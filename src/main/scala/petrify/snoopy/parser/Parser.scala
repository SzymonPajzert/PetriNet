package petrify.snoopy.parser

import petrify.snoopy.model._

trait Parser {
  def places : Iterable[Place]
  def transitions : Iterable[Transition]
  def edges : Iterable[Edge]
}
