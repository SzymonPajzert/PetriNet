package snoopy.parser

import scala.xml.{Text, XML, Node => XmlNode}
import model.{PetriNet, State}
import snoopy.model._
import snoopy.model.ParseOutput._

import scala.util.Try

// TODO create builder from collection of nodes and edges to petri net

object SnoopyParser {
  val defaultCapacity = 3
}

case class SnoopyParser(path: String, defaultCapacity : Int = SnoopyParser.defaultCapacity) {
  val root = XML.loadFile(path)

  private def text(node: XmlNode): Option[String] =
    node.child find { child => child.isInstanceOf[Text] } map { textNode => textNode.text }

  private def extractNode(nodeType: String, node: XmlNode): ParseOutput[Node] = ParseOutput(node) {
    val id = (node \@ "id").toInt
    val nameAttribute = (node \ "attribute") find { attr => attr \@ "name" == "Name" }
    val name = text(nameAttribute.get).get.trim
    nodeType match {
      case "Place" =>
        (node \ "attribute") find { attr => attr \@ "name" == "Comment" } flatMap { node => text(node) } match {
          case None => Place(id, name, defaultCapacity)
          case Some(maxString) =>
            lazy val splitted = maxString.trim.split(" |\n")
            lazy val pairs = splitted zip splitted.tail
            val int = pairs find {
              _._1 == "MAX"
            } map { case (_, intString) => intString.toInt }
            Place(id, name, int.getOrElse(defaultCapacity))
        }
      case "Transition" => Transition(id, name)
      case "Coarse Transition" => Transition(id, name)
      case notSupportedType => throw new TypeNotSupported(notSupportedType)
    }
  }


  private def extractEdge(nodeType: String, node: XmlNode): ParseOutput[Edge]= ParseOutput(node) {
    val id = (node \@ "id").toInt
    val sourceId = (node \@ "source").toInt
    val targetId = (node \@ "target").toInt
    val multiplicity = 1
    Edge(nodeType, id, sourceId, targetId, multiplicity)
  }

  private def extractElt[T](eltName: String, parser: (String, XmlNode) => ParseOutput[T])(root: XmlNode):Iterable[T] = {
    lazy val result = for {
      nodeclass <- root \\ s"${eltName}class"
      nodeType = nodeclass \@ "name"
      node <- nodeclass \ eltName
    } yield parser(nodeType, node)
    val (failures, successes) = result.partition(_.failed)
    println("Parsing failed for: " + failures.length + " nodes")
    if(failures.nonEmpty) {
      println("Failed for:")
      for(Failure(failedNode, explanation) <- failures) {
        println(explanation)
      }
    }

    successes map {_.get}
  }


  private def extractNodes(root : XmlNode) = extractElt("node", extractNode)(root)
  private def extractEdges(root : XmlNode) = extractElt("edge", extractEdge)(root)

  // TODO remove InstanceOf
  lazy val (places, transitions) = extractNodes(root).partition(_.isInstanceOf[Place])
  lazy val edges = extractEdges(root)

  def parse: (Iterable[Place], Iterable[Transition], Iterable[Edge]) = {
    (places map {_.asInstanceOf[Place]}, transitions map {_.asInstanceOf[Transition]}, edges)
  }
}
