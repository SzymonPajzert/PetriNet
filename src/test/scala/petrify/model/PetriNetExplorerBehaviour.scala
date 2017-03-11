package petrify.model

import org.apache.spark.{SparkConf, SparkContext}
import org.scalatest.{BeforeAndAfter, FlatSpec}

import petrify.spark.PetriNetExplorer

class PetriNetExplorerBehaviour extends FlatSpec with BeforeAndAfter {
  import PetriNetTestData._


  var sc : SparkContext = _

  before {
    val conf = new SparkConf()
      .setMaster("local")
      .setAppName("PetrifyBFS")

    sc = new SparkContext(conf)
  }

  after {
    sc.stop()
  }

  def findPlaces(net: PetriNet, begin:State, end:State => Boolean): Unit = {
    assert(PetriNetExplorer.lookFor(sc, net, begin, end))
  }

  def notFindPlaces(net: PetriNet, begin:State, end:State => Boolean): Unit = {
    assert(!PetriNetExplorer.lookFor(sc, net, begin, end))
  }
  
  it should "find places" in {
    assert(PetriNetExplorer.lookFor(sc, net, states(0), { x:State => x == states(2) } )) 
  }
}
