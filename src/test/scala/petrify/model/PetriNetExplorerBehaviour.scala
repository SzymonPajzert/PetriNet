package petrify.model

// TODO switch to petrify.spark

// TODO make it more behavior like

import org.apache.spark.{SparkConf, SparkContext}
import org.scalatest.{BeforeAndAfter, FlatSpec}

import petrify.spark.PetriNetExplorer

class PetriNetExplorerBehaviour extends FlatSpec with BeforeAndAfter {
  import PetriNetTestData._

  var sc : SparkContext = _

  val explorer = PetriNetExplorer(IdentityCompressor)(net)

  before {
    val conf = new SparkConf()
      .setMaster("local")
      .setAppName("PetrifyBFS")

    sc = new SparkContext(conf)
  }

  after {
    sc.stop()
  }

  def findPlaces(begin:State, end:State): Unit = {
    assert(explorer.lookFor(sc, begin, end))
  }

  def notFindPlaces(begin:State, end:State): Unit = {
    assert(explorer.lookFor(sc,begin, end))
  }
  
  it should "find places" in {
    assert(explorer.lookFor(sc, firstState, thirdState))
  }
}
