package petrify.model

import org.scalatest.FunSuite

class PetriNetTest extends FunSuite {
  import PetriNetTestData._

  // TODO move to scala check
  test("testPlaces") {
    val places = net.places
    assert(places contains Pa)
    assert(places contains Pb)
    assert(places contains Pb)
  }

  test("testAddPlace") {
    //TODO
  }

  test("testAddTransition") {
    //TODO
  }

  test("testApply") {
    assert(net.iterate(firstState).toSeq.contains(secondState))
  }

  test("testEmpty") {
    //TODO for any state returns empty consecutive states
  }

}
