package snoopy.parser

import scala.xml.{Node => XmlNode, Text, XML}
import model.PetriNet


// TODO abstract to traits in snoopy models
case class Node(nodeType: String, id: Int, name: String)

case class Edge(edgeType: String, id: Int, sourceId: Int, targetId: Int, multiplicity: Int)

// TODO create builder from collection of nodes and edges to petri net

case class SnoopyParser(path: String) {
  val root = XML.loadFile(path)

  private def text(node : XmlNode):Option[String] =
    node.child find { child => child.isInstanceOf[Text] } map { textNode => textNode.text }

  private def extractNode(nodeType: String, node: XmlNode): Node = {
    val id = (node \@ "id").toInt
    val nameAttribute = (node \ "attribute") find { attr => attr \@ "name" == "Name" }
    val name = text(nameAttribute.get).get.trim
    Node(nodeType, id, name)
  }

  private def extractEdge(nodeType: String, node: XmlNode): Edge = {
    val id = (node \@ "id").toInt
    val sourceId = (node \@ "source").toInt
    val targetId = (node \@ "target").toInt
    val multiplicity = 1
    Edge(nodeType, id, sourceId, targetId, multiplicity)
  }

  private def extractElt[T](eltName: String, parser: (String, XmlNode) => T)(root: XmlNode):Iterable[T]= for {
    nodeclass <- root \\ s"${eltName}class"
    nodeType = nodeclass \@ "name"
    node <- nodeclass \ eltName
  } yield parser(nodeType, node)

  private def extractNodes(root : XmlNode) = extractElt("node", extractNode)(root)
  private def extractEdges(root : XmlNode) = extractElt("edge", extractEdge)(root)

  lazy val nodes = extractNodes(root)
  lazy val edges = extractEdges(root)

  def parse: PetriNet = {
    println(nodes)
    println(edges)
    PetriNet.empty
  }
}
