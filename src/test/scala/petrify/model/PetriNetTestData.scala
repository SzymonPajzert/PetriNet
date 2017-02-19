package petrify.model

import org.scalatest.FlatSpec

object States {

  val Pa = Place("aaa", 1)
  val Pb = Place("bbb", 1)
  val Pc = Place("ccc", 1)
  val Pd = Place("ddd", 1)

  val states : Map[(Int, Int, Int, Int), State] = (for {
    a <- 0 to 1
    b <- 0 to 1
    c <- 0 to 1
    d <- 0 to 1
  } yield ((a, b, c, d) -> State(Pa -> a, Pb -> b, Pc -> c, Pd -> d))).toMap

  def apply(a: Int, b: Int, c:Int, d: Int) = states((a, b, c, d))
}

object PetriNetTestData {
  import States.{Pa, Pb, Pc, Pd}
  
  val Ta = Transition(Pa, Pb)("AAA")(Pc)
  val Tb = Transition(Pc)("BBB")(Pb)
  val Tc = Transition(Pb)("CCC")(Pa, Pb)
  val Td = Transition(Pa)("DDD")() addRead (Pb)
  val Te = Transition()("EEE")(Pb) addInhibitor (Pc) 

  val net = PetriNet(Seq(Pa, Pb, Pc), Seq(Ta, Tb, Tc))

  // TODO Treat states in tests as vectors of three numbers state(1, 1, 0) equals then states(0)
  val states = Vector(
    States(1, 1, 0, 0),
    States(0, 0, 1, 0),
    States(0, 1, 0, 0),
    States(1, 0, 0, 0)
  )
}

class TransitionTest extends FlatSpec with TransitionBehaviour {
  import PetriNetTestData._

  behavior of "Transition A"
  it should behave like activeTransition(Ta, states(0))
  it should behave like correctTransition(Ta, states(0), states(1))
  it should behave like inactiveTransition(Ta, states(1), states(2))

  behavior of "Transition B"
  it should behave like inactiveTransition(Tb, states(0), states(2))
  it should behave like activeTransition(Tb, states(1))
  it should behave like correctTransition(Tb, states(1), states(2))

  behavior of "Transition C"
  it should behave like inactiveTransition(Tc, states(1), states(0))
  it should behave like activeTransition(Tc, states(2))
  it should behave like correctTransition(Tc, states(2), states(0))

  behavior of "Transition D - read edge test"
  it should behave like inactiveTransition(Td, states(1), states(2), states(3))
  it should behave like activeTransition(Td, states(0))
  it should behave like correctTransition(Td, states(0), states(2))

  behavior of "Transition E - inhibitor edge test"
  it should behave like inactiveTransition(Te, States(0,0,1,0), States(0,1,1,0))
  it should behave like activeTransition(Te, States(0,0,0,0), States(1,0,0,0))
  it should behave like correctTransition(Te, States(1,0,0,1), States(1,1,0,1))

  // TODO Inhibitor states
}

class StateTest extends FlatSpec with StateBehaviour {
  import States.{Pa, Pb, Pc, Pd}
  import PetriNetTestData._

  behavior of "First state"
  it should behave like consistentState(states(0), Pa, Pb, Pc)
  it should behave like activeState(states(0), Pa, Pb)

  behavior of "Second state"
  it should behave like consistentState(states(1), Pa, Pb, Pc)
  it should behave like activeState(states(1), Pc)

  behavior of "Third state"
  it should behave like consistentState(states(2), Pa, Pb, Pc)
  it should behave like activeState(states(2), Pb)
}
