package snoopy.model

sealed trait Node {
  def nodeType: String
  def id: Int
  def name: String
}

case class Place(id: Int, name: String, capacity: Int) extends Node {
  override def nodeType: String = "Place"
}

case class Transition(id: Int, name: String) extends Node {
  override def nodeType: String = "Transition"
}
