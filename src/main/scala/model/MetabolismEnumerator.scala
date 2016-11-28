package model

class MetabolismEnumerator(private val places: Iterable[Place]) {
  private val multipliers = {
    val maxIntervals = for(place <- places) yield place.max
    val multipliers = (maxIntervals scanRight 1) {
      case (interval, acc) => interval * acc
    }
    multipliers.tail
  }

  def toInt(state: State): Int = {
    val multiplied = for((place, multiplier) <- state.values zip multipliers) yield place * multiplier
    multiplied.sum
  }

  private def getValues(int :Int): Iterable[Int] = {
    val helper = (multipliers scanLeft (int, 0)) {
      case ((remainder, value), multiplier) => (remainder % multiplier, remainder / multiplier)
    }
    helper.tail map { case (x, y) => y }
  }

  def toState(int: Int): State = {
    (places zip getValues(int) foldLeft State.empty) {
      case (state, (place, value)) => (state incrementPlace (place, value)).get
    }
  }
}


