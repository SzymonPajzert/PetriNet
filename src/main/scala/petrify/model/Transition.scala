package petrify.model

object Transition {
  def apply(name: String):Transition = new Transition(name, Iterable(), Iterable())

  def apply(input: Place*)(name: String)(output: Place*): Transition = new Transition(name, input, output)

  def unapply(arg: Transition): Option[String] = Some(arg.name)
}

class Transition private (val name:String, val input: Iterable[Place], val output: Iterable[Place]) extends Serializable {
  def addInput(additionalInput: Place*):Transition = new Transition(name, input ++ additionalInput, output)

  def addOutput(additionalOutput: Place*):Transition = new Transition(name, input, output ++ additionalOutput)

  def isActive(state: State): Boolean = (state isActive input) && (state isFree output)

  def availableStates(state: State) : Option[State] = {
    (state decrementPlaces input) flatMap (decreasedState => decreasedState incrementPlaces output)
  }

  override def toString:String = s"Transition($name)"
}
