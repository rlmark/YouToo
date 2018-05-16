object Main extends App {
  val wordSalad = new WordSalad()
  val lines = wordSalad.read()
  val tokens = wordSalad.tokenize(lines)
  val ngrams = wordSalad.ngram(2, tokens)
//  ngrams.take(5).foreach(println)
  wordSalad.makeDictionary(ngrams)

  println(wordSalad.makeSentence("She").mkString(" ").replace(" .", "."))
  println(wordSalad.makeSentence("I").mkString(" ").replace(" .", "."))
//  println(wordSalad.makeStatement().mkString(" "))
}
