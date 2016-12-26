package object utilities {

  private val magicNumber = 3

  def convertHash(hash: Int): String = {
    var value = hash
    val chars = for(i <- 1 to magicNumber) yield {
      val newHash = value % 26 + 65
      value = value / 26
      newHash.toChar
    }
    chars mkString ""
  }

  /** Creates nice representation of string being three letter representation of hash
    *
    * @param string String to be represented
    * @return
    */
  def niceRepr(string: String):String = {
    val hash = string.hashCode
    convertHash(hash)
  }


}
