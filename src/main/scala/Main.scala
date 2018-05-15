object Main extends App {
  val wordSalad = new WordSalad()
  val lines = wordSalad.read()
  val tokens = wordSalad.tokenize(lines)
  val ngrams = wordSalad.ngram(2, tokens)
  wordSalad.makeDictionary(ngrams)

  println(wordSalad.makeSentence().mkString(" "))
}
