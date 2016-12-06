package snoopy.model

case class Edge(edgeType: String, id: Int, sourceId: Int, targetId: Int, multiplicity: Int) {
  if(multiplicity != 1) println(s"Found non unit multiplicity = $multiplicity")
}
