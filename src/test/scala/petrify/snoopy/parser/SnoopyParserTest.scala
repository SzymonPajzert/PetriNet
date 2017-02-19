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
      Place(843431,"NR0B2_ActiveGene",3),
      Place(742641,"Cholate_r",10),
      Place(1020251,"NR2B1_DegradedmRNA",3),
      Place(676990,"SRF1",3),
      Place(930153,"HNF4A",3),
      Place(778571,"BAAT_mRNA",3),
      Place(758710,"ABCG5_G8",3),
      Place(786162,"SLCO1B3_ActiveGene",3),
      Place(696152,"FXR_agonist",3),
      Place(776280,"MDR3_DegradedProtein",3),
      Place(785050,"SLCO1B1",3)
    )
 
    assert(subPlaces subsetOf places)
  }

}
