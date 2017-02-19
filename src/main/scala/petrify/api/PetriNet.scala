package petrify.api

import scala.util.Random

object PetriNet {
  type CollectionObservingState = Map[State, (Place => Boolean)]
  type ObservingState = (State, (Place => Boolean))

  private def randomElement[T](iterable: Iterable[T]): T = Random.shuffle(iterable).head
}

trait PetriNet  {
  import PetriNet._

  def places: Set[Place]
  def transitions: Set[Transition]

  final def apply(state: State): State = randomElement(iterate(state))
  final def apply(state: State, places: Place*): ObservingState = randomElement(iterate(state, places: _*))

  def iterate(state: State): Iterable[State]
  def iterate(state: State, places: Place*): CollectionObservingState
}
