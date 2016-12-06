package model

// TODO move to tests.
object Example extends App {

  private def present1(): Unit = {
    val a = Place("aaa", 1)
    val b = Place("bbb", 3)
    val c = Place("ccc", 2)

    val first = Transition.create("first")(a, b)(c)
  }

  present1()
}
