package petrify.model

import org.scalatest.FlatSpec

trait StateBehaviour { this: FlatSpec =>

  def consistentState(state: State, places: Place*) {

    it should "be applicable for its places" in {
      for (place <- places) {
        assert(state isDefinedAt place)
      }
    }

    it should "have non negative tokens" in {
      for (place <- places) {
        assert(state(place) >= 0)
      }
    }

    it should "have tokens not exceeding max value" in {
      for (place <- places) {
        assert(state(place) < place.max)
      }
    }

  }

  def activeState(state: State, activePlaces: Place*) {
    it should "have positive keys for active places" in {
      for (place <- activePlaces) {
        assert(state(place) > 0)
      }
    }

    it should "be active for active places" in {
      assert(state isActive activePlaces)
    }

    it should "contain active places separately in active places" in {
      for(place <- activePlaces) {
        assert(state.activePlaces contains place)
      }
    }
  }
}
