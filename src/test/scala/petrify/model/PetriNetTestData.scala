package petrify.model

import org.scalatest.FlatSpec

object PetriNetTestData {

  val Pa = Place("aaa", 2)
  val Pb = Place("bbb", 3)
  val Pc = Place("ccc", 3)
  val Pd = Place("ddd", 1)

  val Ta = Transition(Pa, Pb)("AAA")(Pc)
  val Tb = Transition(Pc)("BBB")(Pb)
  val Tc = Transition(Pb)("CCC")(Pa, Pb)

  val net = PetriNet(Seq(Pa, Pb, Pc), Seq(Ta, Tb, Tc))

  val firstState = State(Pa -> 1, Pb -> 1, Pc -> 0)
  val secondState = State(Pa -> 0, Pb -> 0, Pc -> 1)
  val thirdState = State(Pa -> 0, Pb -> 1, Pc -> 0)

  val states = Seq(firstState, secondState, thirdState)
}

class TransitionTest extends FlatSpec with TransitionBehaviour {
  import PetriNetTestData._

  behavior of "Transition A"
  it should behave like activeTransition(Ta, firstState)
  it should behave like correctTransition(Ta, firstState, secondState)
  it should behave like inactiveTransition(Ta, secondState, thirdState)

  behavior of "Transition B"
  it should behave like inactiveTransition(Tb, firstState, thirdState)
  it should behave like activeTransition(Tb, secondState)
  it should behave like correctTransition(Tb, secondState, thirdState)

  behavior of "Transition C"
  it should behave like inactiveTransition(Tc, secondState, firstState)
  it should behave like activeTransition(Tc, thirdState)
  it should behave like correctTransition(Tc, thirdState, firstState)

}

class StateTest extends FlatSpec with StateBehaviour {
  import PetriNetTestData._

  behavior of "First state"
  it should behave like consistentState(firstState, Pa, Pb, Pc)
  it should behave like activeState(firstState, Pa, Pb)

  behavior of "Second state"
  it should behave like consistentState(secondState, Pa, Pb, Pc)
  it should behave like activeState(secondState, Pc)

  behavior of "Third state"
  it should behave like consistentState(thirdState, Pa, Pb, Pc)
  it should behave like activeState(thirdState, Pb)
}
