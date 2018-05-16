import scala.annotation.tailrec
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

  type Ngrams = Iterator[Vector[String]]
  def ngram(scale: Int, tokens: Vector[String]): Ngrams = {
    tokens.sliding(scale)
  }

  private var mutableMap: scala.collection.mutable.Map[String, Vector[String]] = scala.collection.mutable.Map.empty

  def makeDictionary(ngrams: Ngrams) = {
    ngrams foreach { ngram: Vector[String] =>
      val w1 +: w2 = ngram
      if (mutableMap.contains(w1)) mutableMap(w1) = mutableMap(w1) ++ w2
      else mutableMap.put(w1, w2)
    }
  }

  def shufflePick(target: String): String = {
    val options: Seq[String] = mutableMap(target)
    Random.shuffle(options).head
  }

  def makeStatement(desiredSentences: Int): Seq[String] = {
    (1 to desiredSentences).foldLeft(Seq.empty[String])((acc, _) => acc ++ makeSentence())
  }

  def makeSentence(seed : String = "I"): Seq[String] = {
    seed +: unfold[String, String](seed) {
      case "." =>
        None
      case (x: String) =>
        val out = shufflePick(x)
        Some((out, out))
    }
  }

  def unfold[A,B](seed: A)(f: A => Option[(A, B)]): Vector[B] = {
    @tailrec
    def go(s: A, v: Vector[B]): Vector[B] = f(s) match {
      case None => v
      case Some((s2, next)) =>
        go(s2, v :+ next)
    }

    go(seed, Vector.empty)
  }
}

