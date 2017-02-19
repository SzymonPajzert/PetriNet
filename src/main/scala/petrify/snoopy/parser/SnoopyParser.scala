package petrify.snoopy.parser

import scala.xml.{Text, XML, Node => XmlNode}
import petrify.snoopy.model._
import petrify.snoopy.model.ParseOutput

object SnoopyParser {
  val defaultCapacity = 3
  val maximumCapacity = None

  def apply(root: XmlNode):SnoopyParser = new SnoopyParser(root)

  def load(path: String):SnoopyParser = SnoopyParser(XML.loadFile(path))

  def text(node: XmlNode): Option[String] = {
    node.child find { child => child.isInstanceOf[Text] } map { textNode => textNode.text }
  }
}

class SnoopyParser(val root: XmlNode,
                   val defaultCapacity:Int = SnoopyParser.defaultCapacity,
                   val maximumCapacity:Option[Int] = SnoopyParser.maximumCapacity)
    extends Parser {

  import SnoopyParser.text

  assert(defaultCapacity <= maximumCapacity.getOrElse(defaultCapacity))

  def trimCapacity(capacityOption: Option[Int]):Int = capacityOption match {
    case None => defaultCapacity
    case Some(capacity) => maximumCapacity match {
      case None => capacity
      case Some(max) => scala.math.min(max, capacity)
    }
  }

  def extractPlace(parsedNode: Node, node: XmlNode): ParseOutput[Place] = {
    val Some(placeMarking) = {
      (node \ "attribute") find
      { attr => attr \@ "name" == "Marking" } flatMap
      { node => text(node) }
    }

    val placeComment = {
      (node \ "attribute") find
      { attr => attr \@ "name" == "Comment" } flatMap
      { node => text(node) }
    }

    val capacity = placeComment match {
      case None => defaultCapacity
      case Some(maxString) =>
        lazy val split = maxString.trim.split(" |\n")
        lazy val pairs = split zip split.tail
        val int = pairs find {_._1 == "MAX"} map { case (_, intString) => intString.toInt }
        trimCapacity(int)
    }

    parsedNode.place(placeMarking.trim.toInt, capacity)
  }

  private def extractNode(nodeType: String, node: XmlNode): ParseOutput[Node] = {
    val id = (node \@ "id").toInt
    val Some(nameAttribute) = (node \ "attribute") find { attr => attr \@ "name" == "Name" }
    val Some(textValue) = text(nameAttribute)
    val name = textValue.trim

    val parsedNode = new Node(id, name)
    nodeType match {
      case "Place" => extractPlace(parsedNode, node)
      case "Transition" => parsedNode.transition()
      case "Coarse Transition" => parsedNode.transition()
      case notSupportedType => Left(node)
    }
  }

  private def extractEdge(nodeType: String, node: XmlNode): ParseOutput[Edge]= {
    val id = (node \@ "id").toInt
    val sourceId = (node \@ "source").toInt
    val targetId = (node \@ "target").toInt
    val multiplicity = 1
    Edge(EdgeType(nodeType), id, sourceId, targetId, multiplicity)
  }

  type Parser[T] = (String, XmlNode) => ParseOutput[T]

  private def extractElt[T](eltName: String, parser: Parser[T])(root: XmlNode):Iterable[T] = {
    val result = for {
      nodeclass <- root \\ s"${eltName}class"
      nodeType = nodeclass \@ "name"
      node <- nodeclass \ eltName
    } yield parser(nodeType, node)

    val (failures, successes) = (
      result collect { case Left(node) => node },
      result collect { case Right(output) => output }
    )

    // TODO somehow propagate parsing fails println(s"Parsing failed for: ${failures.length} nodes")
    if(failures.nonEmpty) {
      println("Failed for:")
      for(node <- failures) {
        println(node)
      }
    }

    successes
  }

  private def extractNodes(root : XmlNode) = extractElt("node", extractNode)(root)
  private def extractEdges(root : XmlNode) = extractElt("edge", extractEdge)(root)

  val (placesOpt, transitionsOpt) = (extractNodes(root) map {
    case transition : Transition => (None, Some(transition))
    case place : Place => (Some(place), None)
  }).unzip

  override val places = placesOpt.flatten
  override val transitions = transitionsOpt.flatten
  override val edges = extractEdges(root)
}
