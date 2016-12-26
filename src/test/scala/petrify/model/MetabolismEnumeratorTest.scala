package petrify.model

import org.scalatest.FunSuite

class MetabolismEnumeratorTest extends FunSuite {

  private val places = Iterable(Place("aaa", 2), Place("bbb", 2), Place("ccc", 3))
  private val enumerator = new MetabolismEnumerator(places)

  test("testToInt") {
  }

  test("testToInt spawns different integers") {
    val a :: b :: c :: _ = places
    val states = for {
      i_a <- 0 until a.max
      i_b <- 0 until b.max
      i_c <- 0 until c.max
    } yield State(a -> i_a, b -> i_b, c -> i_c)

    assert((states map enumerator.toInt).distinct.length == states.length)
  }

  test("testToState") {

  }

}
