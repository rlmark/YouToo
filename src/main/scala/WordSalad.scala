import scala.annotation.tailrec
import scala.collection.immutable
import scala.io.Source
import scala.util.Random

class WordSalad(source: Source.type) {
  def read(): String = {
    source.fromResource("statements").getLines().mkString("")
  }

  def tokenize(string: String): Vector[String] = {
    string.replaceAll("\\."," .").replaceAll("[\\,|;|:|'|\"|”]", "").split(" ").toVector
  }

  type Ngrams = Iterator[Vector[String]]
  def ngram(scale: Int, tokens: Vector[String]): Ngrams = {
    tokens.sliding(scale)
  }

  var mutableMap: scala.collection.mutable.Map[(String, String), Vector[String]] = scala.collection.mutable.Map.empty

  def makeDictionary(ngrams: Ngrams) = {
    ngrams foreach { ngram: Vector[String] =>
      val w1 +: tail = ngram
      if (mutableMap.contains( (w1, tail.head ))) mutableMap( (w1, tail.head)) = mutableMap((w1, tail.head)) ++ tail.tail
      else mutableMap.put(w1 -> tail.head, tail.tail)
    }
  }

  def shufflePick(target: (String, String)): (String) = {
    val candidates: Option[Vector[String]] = mutableMap.get(target)

    val maybeNext = candidates.map { foundNext: immutable.Seq[String] =>
      Random.shuffle(foundNext).head
    }

    lazy val randomValue: String = Random.shuffle(mutableMap.keys).head._1
    maybeNext.getOrElse(randomValue)
  }

  def makeStatement(desiredSentences: Int): Seq[String] = {
    (1 to desiredSentences).foldLeft(Seq.empty[String])((acc, _) => acc ++ makeSentence())
  }

  def makeSentence(seed : (String, String) = ("I", "am")): Seq[String] = {
    "I am" +: unfold[(String, String), String](seed) {
      case (_, ".") =>
        None
      case (w1, w2) =>
        val (out1) = shufflePick(w1,w2)
        Some((w2, out1), out1)
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

