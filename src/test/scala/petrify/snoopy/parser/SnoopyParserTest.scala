package petrify.snoopy.parser

import scala.xml.XML

import org.scalatest.FunSuite

import petrify.snoopy.model._

class SnoopyParserTest extends FunSuite {

  val node : scala.xml.Node = XML.loadFile(getClass.getResource("/HepatocyteQSSPN.spept").getPath)

  val parser = SnoopyParser(node)

  val places = parser.places.toSet
  val transitions = parser.transitions.toSet
  val edges = parser.edges.toSet

  test("testTransitions") {
    assert(transitions contains Transition( 930975,"NR5A2_translation"))
  }

  test("testEdges") {
    val subEdges = Set(
      Edge(InhibitorEdge, 856837, 856557, 856663, 1),
      Edge(ReadEdge, 679130, 677254, 677888, 1),
      Edge(NormalEdge, 782108, 781931, 782027, 1),
      Edge(NormalEdge, 1020006, 1019973, 1019920, 1)
    )

    assert(subEdges subsetOf edges)
  }

  // TODO Check if results are good
  test("testPlaces") {
    val subPlaces = Set(
      Place(676781,"DR4",1,3),
      Place(779729,"ABCC2_mRNA",0,3),
      Place(782973,"CYP8B1_DegradedmRNA",0,3),
      Place(1020251,"NR2B1_DegradedmRNA",0,3),
      Place(697915,"AAs",1,3),
      Place(785050,"SLCO1B1",1,3),
      Place(996255,"NR1H3_mRNA",0,3),
      Place(1021300,"NR1H4_DegradedmRNA",0,3),
      Place(676990,"SRF1",1,3),
      Place(677210,"EGFR",0,3),
      Place(1019942,"NR2B1_mRNA",0,3),
      Place(677276,"SOS",1,3),
      Place(781931,"CYP27A1_ActiveGene",0,3),
      Place(677331,"Ca_ion_intra",1,3),
      Place(793860,"SLC10A1_mRNA",0,3),
      Place(770344,"UGTB4_mRNA",0,3),
      Place(784020,"CYP7A1_DegradedmRNA",0,3),
      Place(1019953,"NR2B1",1,3)
    )
 
    assert(subPlaces subsetOf places)
  }

}
