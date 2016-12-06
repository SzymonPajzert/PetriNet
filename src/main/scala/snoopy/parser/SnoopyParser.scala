package snoopy.parser

import scala.xml.{PCData, XML, Node => XmlNode}
import snoopy.model._
import snoopy.model.ParseOutput._

object SnoopyParser {
  val defaultCapacity = 3
  val maximumCapacity = None

  def apply(root: XmlNode):SnoopyParser = new SnoopyParser(root)

  def load(path: String):SnoopyParser = SnoopyParser(XML.loadFile(path))

  def text(node: XmlNode): Option[String] =
    node.child find { child => child.isInstanceOf[PCData] } map { textNode => textNode.text }
}

class SnoopyParser(val root: XmlNode,
                   val defaultCapacity:Int = SnoopyParser.defaultCapacity,
                   val maximumCapacity:Option[Int] = SnoopyParser.maximumCapacity) {
  import SnoopyParser.text

  assert(defaultCapacity <= maximumCapacity.getOrElse(defaultCapacity))

  private def trimCapacity(capacity: Int):Int = maximumCapacity match {
    case None => capacity
    case Some(max) => scala.math.min(max, capacity)
  }

  private def trimCapacity(capacityOption: Option[Int]):Int = capacityOption match {
    case None => defaultCapacity
    case Some(capacity) => trimCapacity(capacity)
  }

  private def extractPlace(parsedNode: Node, node: XmlNode): ParseOutput[Place] = ParseOutput(node) {
    (node \ "attribute") find { attr => attr \@ "name" == "Comment" } flatMap { node => text(node) } match {
      case None => parsedNode.place(defaultCapacity)
      case Some(maxString) =>
        lazy val split = maxString.trim.split(" |\n")
        lazy val pairs = split zip split.tail
        val int = pairs find {_._1 == "MAX"} map { case (_, intString) => intString.toInt }
        val capacity = trimCapacity(int)
        parsedNode.place(capacity)
    }
  }

  private def extractNode(nodeType: String, node: XmlNode): ParseOutput[(Option[Place], Option[Transition])] = ParseOutput(node) {
    val id = (node \@ "id").toInt
    val nameAttribute = (node \ "attribute") find { attr => attr \@ "name" == "Name" }
    val name = text(nameAttribute.get).get.trim
    val parsedNode = new Node(id, name)
    nodeType match {
      case "Place" => (Some(extractPlace(parsedNode, node).get), None)
      case "Transition" => (None, Some(parsedNode.transition()))
      case "Coarse Transition" => (None, Some(parsedNode.transition()))
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

  lazy val (placesOpt, transitionsOpt) = extractNodes(root).unzip
  lazy val places = placesOpt.flatten
  lazy val transitions = transitionsOpt.flatten
  lazy val edges = extractEdges(root)

  def parse: (Iterable[Place], Iterable[Transition], Iterable[Edge]) = (places, transitions, edges)
}
