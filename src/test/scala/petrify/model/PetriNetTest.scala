package petrify.model

import org.scalatest.FunSuite

class PetriNetTest extends FunSuite {
  import PetriNetTestData._
  import States.{Pa, Pb, Pc, Pd}

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
    assert(net.iterate(states(0)).toSeq.contains(states(1)))
  }

  test("testEmpty") {
    //TODO for any state returns empty consecutive states
  }

}
