package utilities

class package$Test extends org.scalatest.FunSuite {
  test("Int to *** conversion") {
    val pairs = List(
      (0, "AAA"),
      (1, "BAA"),
      (26, "ABA"),
      (26*26, "AAB")
    )
    for( (hash, value) <- pairs ) {
      assert(convertHash(hash) == value)
    }
  }
}
