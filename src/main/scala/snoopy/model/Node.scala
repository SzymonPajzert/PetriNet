package snoopy.model

private[snoopy] sealed class Node(val id: Int, val name: String) {
  def place(capacity: Int):Place = Place(id, name, capacity)
  def transition():Transition = Transition(id, name)
}

case class Place(override val id: Int, override val name: String, capacity: Int) extends Node(id, name)

case class Transition(override val id: Int, override val name: String) extends Node(id, name)
