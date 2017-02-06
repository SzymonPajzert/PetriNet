package petrify.spark

import scala.reflect.ClassTag

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}
import petrify.model._

object PetriNetExplorer {
  def apply[T:ClassTag](compressorFactory: StateCompressorCompanion[StateCompressor[T]])(petriNet: PetriNetAPI):PetriNetExplorer[T] = {
    val compressor = compressorFactory(petriNet)
    new PetriNetExplorer(compressor, petriNet)
  }
}

class PetriNetExplorer[T:ClassTag](compressor: StateCompressor[T], val petriNet: PetriNetAPI) {
  
  def lookForHelper(pastStates: RDD[T], toIterate: RDD[T], end: T):Boolean = {
    def iteration(compressed : T):Iterable[T] = {
      val decompressed = compressor decompress compressed
      (petriNet iterate decompressed) map (compressor compress)
    }

    val nextStates = (toIterate flatMap iteration subtract pastStates).persist()
    if (nextStates.isEmpty()) false else {
      if(nextStates.filter(_ == end).count() > 0) true else {
        val newPastStates = pastStates union nextStates
        lookForHelper(newPastStates, nextStates, end)
      }
    }
  }

  def lookFor(sc: SparkContext, start: State, end: State): Boolean = {
    val comStart = compressor compress start
    val comEnd = compressor compress end

    val initial = sc.parallelize(Seq(comStart))
    lookForHelper(sc.parallelize(Seq()), initial, comEnd)
  }

}
