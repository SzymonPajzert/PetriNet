package petrify.spark

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}
import petrify.model.{PetriNet, Place, State, Transition}

/**
  * Created by szymonpajzert on 27.12.16.
  */
object PetriNetExplorer {
  def lookForHelper(petriNet: PetriNet, pastStates: RDD[State], toIterate: RDD[State], end: State):Boolean = {
    val nextStates = toIterate.flatMap(petriNet.iterate).subtract(pastStates).persist()
    if (nextStates.isEmpty()) false else {
      if(nextStates.filter(_ == end).count() > 0) true else {
        val newPastStates = pastStates union nextStates
        lookForHelper(petriNet, newPastStates, nextStates, end)
      }
    }
  }

  def lookFor(sc: SparkContext, petriNet: PetriNet, start: State, end: State):Boolean = {
    val initial = sc.parallelize(Seq(start))
    lookForHelper(petriNet, sc.parallelize(Seq()), initial, end)
  }
}
