package petrify.model

sealed trait StateCompressorCompanion[+Compressor <: StateCompressor[_]] {
  def apply(net: PetriNetAPI): Compressor
}

abstract class StateCompressor[T](val places: Seq[Place]) {
  def compress(state: State): T
  def decompress(value: T): State
}

object BitArrayCompressor extends StateCompressorCompanion[BitArrayCompressor] {
  private def neededSize(maxValue: Int): Int = {
    import scala.math.{log, ceil}
    ceil(log(maxValue + 1) / log(2.0)).toInt + 1
  }

  def apply(net: PetriNetAPI): BitArrayCompressor = {
    new BitArrayCompressor(net.places.toSeq)
  }
}

class BitArrayCompressor(override val places: Seq[Place]) extends StateCompressor[Array[Boolean]](places) {
  import BitArrayCompressor._
  
  private val sizes = places map { x => neededSize(x.max) }
  private val allIndexes = (sizes scanLeft 0) { (acc, size) => (acc + size) }
  private val indexRanges = allIndexes.dropRight(1) zip allIndexes.drop(1)
  private val size = sizes.sum

  def compress(state: State): Array[Boolean] = {
    val result = new Array[Boolean](size)
    for ((place, (firstIndex, lastIndex)) <- places zip indexRanges) {
      var value = state(place)
      for(index <- lastIndex to firstIndex by -1) {
        result(index) = (value % 2) == 1
        value = value / 2
      }
    }
    result
  }

  implicit private def bool2int(b:Boolean):Int = if (b) 1 else 0


  def decompress(array: Array[Boolean]): State = {
    var states = List[(Place, Int)]()
    for((place, (firstIndex, lastIndex)) <- places zip indexRanges) {
      var value = 0
      for(index <- (firstIndex until lastIndex)) {
        value *= 2
        value = array(index)
      }
      states = (place, value) :: states
    }
    State.create(states)
  }
}

/*
class IntCompressor(override val net: PetriNetAPI) extends StateCompressor[Int](net) {
  private val multipliers : Map[Place, Int] = {
    val maxIntervals = for(place <- places) yield place.max
    val multipliers = (places scanRight 1) {
      case (interval, acc) => interval * acc
    }
    multipliers.drop(1)
  }

  def compress(state: State): Int = {
    val multiplied = for((place, value) <- state) yield multiplier(place) * value
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

  def compress(state: State): Int = {

  }
}
 */

object IdentityCompressor extends StateCompressorCompanion[IdentityCompressor] {
  def apply(net: PetriNetAPI): IdentityCompressor = {
    new IdentityCompressor
  }
  
}

class IdentityCompressor extends StateCompressor[State](Seq())  {
  def compress(state: State): State = state
  def decompress(value: State): State = value
}
