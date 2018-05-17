

object Main extends App {
  val equivocator = new Equivocate()
  val lines = equivocator.read()
  val tokens = equivocator.tokenize(lines)
  val ngrams = equivocator.ngram(tokens)
  equivocator.makeDictionary(ngrams)

  println(equivocator.makeSentence().mkString(" ").replace(" .", "."))
}
//I am sorry for the truthiness of the truth is I am blessed to be untrue.
//I am not some innocent bystander I am in a position of significant power.