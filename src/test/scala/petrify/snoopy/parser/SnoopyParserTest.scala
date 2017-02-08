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

  println(places)
  println(transitions)
  println(edges)

  test("testTransitions") {
    assert(transitions contains Transition( 930975,"NR5A2_translation"))
  }

  test("testEdges") {
    //TODO
  }

  test("testPlaces") {
    
  }

  // TODO test of maximum capacity
}
