package petrify.model

import scala.util.Random

object PetriNetAPI {
  type CollectionObservingState = Map[State, (Place => Boolean)]
  type ObservingState = (State, (Place => Boolean))
}

trait PetriNetAPI {
  import PetriNetAPI._

  private def randomElement[T](iterable: Iterable[T]): T = Random.shuffle(iterable).head

  final def apply(state: State): State = randomElement(iterate(state))
  final def apply(state: State, places: Place*): ObservingState = randomElement(iterate(state, places: _*))

  def iterate(state: State): Iterable[State]
  def iterate(state: State, places: Place*): CollectionObservingState
}
