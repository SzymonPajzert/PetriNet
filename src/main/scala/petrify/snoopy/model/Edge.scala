package petrify.snoopy.model

sealed trait EdgeType

object EdgeType {
  def apply(name: String): EdgeType = {
    name match {
      case "Edge" => NormalEdge
      case "Read Edge" => ReadEdge
      case "Inhibitor Edge" => InhibitorEdge
    }
  }
}

final case object NormalEdge extends EdgeType
final case object ReadEdge extends EdgeType
final case object InhibitorEdge extends EdgeType

case class Edge(edgeType: EdgeType, id: Int, sourceId: Int, targetId: Int, multiplicity: Int) {
  if(multiplicity != 1) println(s"Found non unit multiplicity = $multiplicity")
}
