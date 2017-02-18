package petrify.snoopy


import java.io.InputStream
import petrify.model.{PetriNet, State}

import scala.xml.XML

package object parser {

  def tupleToParseResult(tuple: (PetriNet, State)) : ParseResult = {
    ParseResult(tuple._1, tuple._2)
  }

  def read(file: InputStream):(PetriNet, State) = {
    val parser = SnoopyParser(XML.load(file))
    val builder = SnoopyBuilder(parser.places, parser.transitions, parser.edges)
    builder.build
  }

  // TODO make separate parsing for PetriNet and State
  def readNet(file:InputStream):PetriNet = read(file)._1
  def readState(file:InputStream):State = read(file)._2
}
