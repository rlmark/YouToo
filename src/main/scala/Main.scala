object Main extends App {
  val wordSalad = new WordSalad()
  val lines = wordSalad.read()
  val tokens = wordSalad.tokenize(lines)
  val ngrams = wordSalad.ngram(3, tokens)
  wordSalad.makeDictionary(ngrams)
  wordSalad.mutableMap.take(150) foreach println

  println(wordSalad.makeSentence().mkString(" ").replace(" .", "."))
}
