package petrify.model

case class Place(name:String, max: Int) extends Ordered[Place] with Serializable {
  override def compare(that: Place): Int = this.name compare that.name match {
    case 0 => this.max compare that.max
    case v => v
  }
}
