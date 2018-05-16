import scala.io.Source

object Main extends App {
  val equivocator = new Equivocate(Source)
  val lines = equivocator.read()
  val tokens = equivocator.tokenize(lines)
  val ngrams = equivocator.ngram(3, tokens)
  equivocator.makeDictionary(ngrams)

  println(equivocator.makeSentence().mkString(" ").replace(" .", "."))
}
