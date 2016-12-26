package petrify.snoopy.model

import scala.xml.{Node => XmlNode}

sealed trait ParseOutput[+T] {
  def failed: Boolean
  def get:T
}
object ParseOutput {
  case class Success[+T](result: T) extends ParseOutput[T] {
    def failed:Boolean = false

    def get: T = result
  }
  case class Failure(node: XmlNode, explanation:String = "Unavailable") extends ParseOutput[Nothing] {
    def failed:Boolean = true
    def get:Nothing = throw new NoSuchElementException("Failure.get")
  }

  def apply[T](node: XmlNode)(expression: => T): ParseOutput[T] = try {
    Success(expression)
  } catch {
    case TypeNotSupported(typeName) => Failure(node, "Type \"" + typeName + "\" is not supported")
    case e:Exception => Failure(node, e.getLocalizedMessage)
  }

  case class TypeNotSupported(typeName: String) extends Exception
}
