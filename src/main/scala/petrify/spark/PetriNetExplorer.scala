package petrify.spark

import scala.reflect.ClassTag

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

import petrify.model.{PetriNet, Place, State, Transition}
import petrify.api.GenericPetriNet

object PetriNetExplorer {
  def lookForHelper[StateT:ClassTag](petriNet: GenericPetriNet[StateT], pastStates: RDD[StateT], toIterate: RDD[StateT], end: StateT => Boolean):Boolean = {
    val nextStates = toIterate.flatMap(petriNet.iterate).subtract(pastStates).persist()
    if (nextStates.isEmpty()) false else {
      ((nextStates filter end).count() > 0) || {
        val newPastStates = pastStates union nextStates
        lookForHelper(petriNet, newPastStates, nextStates, end)
      }
    }
  }

  def lookFor[StateT:ClassTag](sc: SparkContext, petriNet: GenericPetriNet[StateT], start: StateT, end: StateT => Boolean):Boolean = {
    val initial = sc.parallelize(Seq(start))
    lookForHelper(petriNet, sc.parallelize(Seq[StateT]()), initial, end)
  }
}
