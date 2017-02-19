package petrify.model

import org.scalatest.FunSuite

class PetriNet$Test extends FunSuite{
  import States.{Pa, Pb, Pc, Pd}
  import PetriNetTestData._

  test("testEmpty") {
    val empty = PetriNet.empty
    val places = empty.places
    val transitions = empty.transitions

    assert(!places.contains(Pa))
    assert(!places.contains(Pb))
    assert(!places.contains(Pc))
    assert(!transitions.contains(Ta))
    assert(!transitions.contains(Tb))
  }

  /* TODO
  test("testApply") {

  }*/

}
