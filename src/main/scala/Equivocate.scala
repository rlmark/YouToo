import monix.eval.Task

import scala.annotation.tailrec
import scala.collection.immutable
import scala.collection.mutable.{Map => MutableMap}
import scala.io.Source
import scala.util.Random

class Equivocate(random: Random) {
  private val ORDER = 3 // DON'T CHANGE
  type Ngrams = Iterable[Vector[String]]

  def read(): Task[String] = {
    Task(Source.fromResource("statements").getLines().mkString(""))
  }

  def tokenize(string: String): Vector[String] = {
    string.replaceAll("\\.", " .").replaceAll("[,;:'\"”“()]", "").split(" ").toVector
  }

  def ngram(tokens: Vector[String]): Ngrams = {
    tokens.sliding(ORDER).toIterable
  }

  def makeDictionary(ngrams: Ngrams) = {
    var mutableMap: MutableMap[(String, String), Vector[String]] = scala.collection.mutable.Map.empty
    ngrams foreach { ngram: Vector[String] =>
      val w1 +: w2 +: tail = ngram
      if (mutableMap.contains((w1, w2))) mutableMap( (w1, w2)) = mutableMap((w1, w2)) ++ tail
      else mutableMap.put(w1 -> w2, tail)
    }
    mutableMap
  }

  def shufflePick(target: (String, String), dictionary: MutableMap[(String, String), Vector[String]]): (String) = {
    val candidates: Option[Vector[String]] = dictionary.get(target)

    val maybeNext = candidates.map { foundNext: immutable.Seq[String] =>
      random.shuffle(foundNext).head
    }

    lazy val randomValue: String = random.shuffle(dictionary.keys).head._1
    maybeNext.getOrElse(randomValue)
  }

  def makeStatement(desiredSentences: Int, dictionary: MutableMap[(String, String), Vector[String]]): Seq[String] = {
    (1 to desiredSentences).foldLeft(Seq.empty[String])((acc, _) => acc ++ makeSentence(dictionary))
  }

  def makeSentence(dictionary: MutableMap[(String, String), Vector[String]], seed : (String, String) = ("I", "am")): Seq[String] = {
    s"${seed._1} ${seed._2}" +: unfold[(String, String), String](seed) {
      case (_, ".") =>
        None
      case (w1, w2) =>
        val (out1) = shufflePick((w1,w2), dictionary)
        Some((w2, out1), out1)
    }
  }

  def run(): Task[String] = {
    for {
      lines <- read()
      tokens = tokenize(lines)
      ngrams = ngram(tokens)
      dictionary = makeDictionary(ngrams)
      sentence = makeSentence(dictionary).mkString(" ").replace(" .", ".")
    } yield sentence
  }

  private def unfold[A, B](seed: A)(f: A => Option[(A, B)]): Vector[B] = {
    @tailrec
    def go(s: A, v: Vector[B]): Vector[B] = f(s) match {
      case None => v
      case Some((s2, next)) =>
        go(s2, v :+ next)
    }

    go(seed, Vector.empty)
  }
}

