import scala.collection.immutable
import scala.io.Source
import scala.util.Random

class Parse {
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
  val p = new Parse()
  val lines = p.read()
  val tokens = p.tokenize(lines)
  val ngrams = p.ngram(2, tokens)
  p.makeDictionary(ngrams)

  println((1 to 50).foldLeft(p.shufflePick("I")){(str, acc) =>
    println(str)
    p.shufflePick(str)})


}