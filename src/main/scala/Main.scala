import scala.util.Random

object Main extends App {
  val random = new Random()
  val equivocator = new Equivocate(random)
  val lines = equivocator.read()
  val tokens = equivocator.tokenize(lines)
  val ngrams = equivocator.ngram(tokens)
  val dictionary = equivocator.makeDictionary(ngrams)

  println(equivocator.makeSentence(dictionary).mkString(" ").replace(" .", "."))
}
// ~~~!*)()@#(FAVES!*)()@#(~~~
//I am sorry for the truthiness of the truth is I am blessed to be untrue.
//I am not some innocent bystander I am in a position of significant power.
//I am confident that these women know I haven’t been sober for more than 5 years.
//I am cooperating with the House of Representatives through hyperbolized public excoriation.
//I am leaving while a man of violence.
