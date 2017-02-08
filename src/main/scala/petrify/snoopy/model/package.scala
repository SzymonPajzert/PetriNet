package petrify.snoopy

import scala.xml.{Node => XmlNode}

/*
sealed trait ParseDescription {

}

case class TypeNotSupported(typeName: String) extends ParseDescription
 */

package object model {
  type ParseOutput[T] = Either[XmlNode, T]

  implicit def typeRightHand[T](value: T): ParseOutput[T] = Right(value)
}
