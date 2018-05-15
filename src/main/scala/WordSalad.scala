import scala.collection.immutable
import scala.io.Source
import scala.util.Random

class WordSalad {
  def read(): String = {
    Source.fromResource("statements").getLines().mkString("")
  }

  def tokenize(string: String): Vector[String] = {
    string.replaceAll("\\."," .").replaceAll("[\\,|;|:|'|\"]", "").split(" ").toVector
  }

  def ngram(scale: Int, tokens: Vector[String]): Iterator[Vector[String]] = {
    tokens.sliding(scale)
  }
  private var mutableMap:  scala.collection.mutable.Map[String, Vector[String]] = scala.collection.mutable.Map.empty

  type Ngrams = Iterator[Vector[String]]
  def makeDictionary(ngrams: Iterator[Vector[String]]) = {
    ngrams foreach {ngram: immutable.Seq[String] =>
      val w1 = ngram(0)
      val w2 = ngram(1)
      if (mutableMap.contains(w1)) { mutableMap(w1) = mutableMap(w1) :+ w2 }
      else mutableMap.put(w1, Vector(w2))
    }
    mutableMap
  }

  def shufflePick(target: String): String = {
    val options: Seq[String] = mutableMap(target)
    Random.shuffle(options).head
  }
}

object Main extends App {
  val wordSalad = new WordSalad()
  val lines = wordSalad.read()
  val tokens = wordSalad.tokenize(lines)
  val ngrams = wordSalad.ngram(2, tokens)
  wordSalad.makeDictionary(ngrams)

  println((1 to 50).foldLeft(wordSalad.shufflePick("I")){ (str, next) =>
    println(str)
    wordSalad.shufflePick(str)})


}