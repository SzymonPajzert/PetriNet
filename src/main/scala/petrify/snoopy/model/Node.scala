package petrify.snoopy.model

sealed class Node(val id: Int, val name: String) {
  def place(marking: Int, capacity: Int):Place = Place(id, name, marking, capacity)
  def transition():Transition = Transition(id, name)
}

final case class Place(override val id: Int, override val name: String, marking: Int, capacity: Int) extends Node(id, name)

final case class Transition(override val id: Int, override val name: String) extends Node(id, name)
