package model

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
