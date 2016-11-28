package model

object Example extends App {

  private def present1(): Unit = {
    val a = Place("aaa", 1)
    val b = Place("bbb", 3)
    val c = Place("ccc", 2)

    val first = Transition("first")(a, b)(c)
  }

  override def main(args: Array[String]): Unit = {
    present1()
  }
}
