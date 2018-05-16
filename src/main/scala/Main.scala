import scala.io.Source

object Main extends App {
  val wordSalad = new WordSalad(Source)
  val lines = wordSalad.read()
  val tokens = wordSalad.tokenize(lines)
  val ngrams = wordSalad.ngram(3, tokens)
  wordSalad.makeDictionary(ngrams)

  println(wordSalad.makeSentence().mkString(" ").replace(" .", "."))
}
