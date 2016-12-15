package model

import org.scalatest.FlatSpec

trait TransitionBehaviour { this: FlatSpec =>

  def inactiveTransition(transition: Transition, states: State*) {

    it should "be inactive" in {
      for(state <- states) {
        println(state)
        assert(! (transition isActive state))
      }
    }

    it should "yield empty result" in {
      for(state <- states) {
        assert((transition availableStates state).isEmpty)
      }
    }
  }

  def activeTransition(transition: Transition, states: State*) {
    it should "be activated" in {
      for(state <- states) {
        assert(transition isActive state)
      }
    }

    it should "yield non empty results" in {
      for(state <- states) {
        println(state)
        assert((transition availableStates state).nonEmpty)
      }
    }
  }

  def correctTransition(transition: Transition, inputState: State, outputStates: State*) {
    it should "calculate correct state" in {
      val states = (transition availableStates inputState).toSet

      for(outputState <- outputStates) {
        assert(states contains outputState)
      }
    }
  }

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
