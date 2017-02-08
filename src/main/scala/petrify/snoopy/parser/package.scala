package petrify.snoopy

import petrify.model.{PetriNet, State}

import scala.xml.XML

package object parser {

  def tupleToParseResult(tuple: (PetriNet, State)) : ParseResult = {
    ParseResult(tuple._1, tuple._2)
  }


  def read(file:String):(PetriNet, State) = {
    val parser = SnoopyParser(XML.loadFile(file))
    val builder = SnoopyBuilder(parser.places, parser.transitions, parser.edges)
    builder.build
  }

  // TODO make separate parsing for PetriNet and State
  def readNet(file:String):PetriNet = read(file)._1
  def readState(file:String):State = read(file)._2
}
