package petrify.model

class MetabolismEnumerator(private val places: Iterable[Place]) {
  private val multipliers = {
    val maxIntervals = for(place <- places) yield place.max
    val multipliers = (maxIntervals scanRight 1) {
      case (interval, acc) => interval * acc
    }
    multipliers.drop(1)
  }

  def toInt(state: State): Int = {
    val multiplied = for((place, multiplier) <- state.values zip multipliers) yield place * multiplier
    multiplied.sum
  }

  private def getValues(int :Int): Iterable[Int] = {
    val helper = (multipliers scanLeft (int, 0)) {
      case ((remainder, _), multiplier) => (remainder % multiplier, remainder / multiplier)
    }
    helper.drop(1) map (_._2)
  }

  def toState(int: Int): State = {
    State.create(places zip getValues(int))
  }
}


