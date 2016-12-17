package model

import org.scalatest.FunSuite

class PetriNetTest extends FunSuite {
  import PetriNetTestData._

  test("testGetPlace") {
    assert(net.getPlace("aaa").contains(Pa))
    assert(net.getPlace("bbb").contains(Pb))
    assert(net.getPlace("ccc").contains(Pc))
    assert(!net.getPlace("ddd").contains(Pd))
  }

  // TODO move to scala check
  test("testPlaces") {
    val places = net.places.values.toSeq
    assert(places.sorted == Seq(Pa, Pb, Pc).sorted)
  }

  test("testGetTransition") {
    assert(net.getTransition("AAA").contains(Ta))
    assert(net.getTransition("BBB").contains(Tb))
    assert(net.getTransition("CCC").contains(Tc))
  }

  test("testAddPlace") {
    //TODO
  }

  test("testAddTransition") {
    //TODO
  }

  test("testApply") {
    assert(net(firstState).toSeq.contains(secondState))
  }

  test("testActiveTransitions") {
    val Seq(firstActive, secondActive, thirdActive) = for(state <- states) yield net.activeTransitions(state).toSeq
    assert(firstActive.contains(Ta))
    assert(secondActive.contains(Tb))
    assert(thirdActive.contains(Tc))
  }


  test("testEmpty") {
    //TODO for any state returns empty consecutive states
  }

}
