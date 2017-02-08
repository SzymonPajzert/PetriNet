package petrify.snoopy

import petrify.model.{PetriNet, State}

import scala.xml.XML

/**
  * Created by szymonpajzert on 27.12.16.
  */
package object parser {
  def read(file:String):(PetriNet, State) = {
    val parser = SnoopyParser(XML.loadFile(file))
    val builder = SnoopyBuilder(parser.places, parser.transitions, parser.edges)
    builder.build
  }

  // TODO make separate parsing for PetriNet and State
  def readNet(file:String):PetriNet = read(file)._1
  def readState(file:String):State = read(file)._2
}
