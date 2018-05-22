import org.mockito.Mockito._
import org.scalatest.mockito.MockitoSugar
import org.scalatest.{Matchers, WordSpec}

import scala.collection.generic.CanBuildFrom
import scala.collection.mutable.{Map => MutableMap}
import scala.util.Random

class EquivocateSpec extends WordSpec with Matchers with MockitoSugar {

  def withMocks(f: (Equivocate, Random) => Unit): Unit = {
    val mockRandom = mock[Random]
    val e = new Equivocate(mockRandom)
    f(e, mockRandom)
  }

  "tokenize" should {
    "split text into a Vector" in withMocks { (equivocator, _) =>
      val sampleString = "this is a test."
      equivocator.tokenize(sampleString) shouldBe Vector("this", "is", "a", "test", ".")
    }
    "strip punctuation except sentence endings" in withMocks { (equivocator, _) =>
      val sampleString = "hey, ();contains: punctuation()."
      equivocator.tokenize(sampleString) shouldBe Vector("hey", "contains", "punctuation", ".")
    }
  }
  "ngram" should {
    "take tokens and turn them into Trigrams" in withMocks { (equivocator, _) =>
      val sampleTokens = Vector("this", "is", "a", "test", ".")
      val expected = List(Vector("this", "is", "a"), Vector("is", "a", "test"), Vector("a", "test", "."))
      equivocator.ngram(sampleTokens).toList shouldBe expected
    }
  }
  "makeDictionary" should {
    val ngrams = Iterable(
      Vector("this", "is", "a"),
      Vector("is", "a", "test"),
      Vector("a", "test", "b"),
      Vector("test", "b", "c"),
      Vector("b","c","this"),
      Vector("c","this","is"),
      Vector("this","is","z"))
    "take ngrams and turn them into a dictionary with appropriate keys" in withMocks { (equivocator, _) =>
      val dictionary = equivocator.makeDictionary(ngrams)
      dictionary.keys.toSet shouldBe Set("this" ->"is", "is" -> "a", "a" -> "test", "test" -> "b", "b"->"c", "c"->"this")
    }
    "take ngrams and turn them into a dictionary that handles single values for a key" in withMocks { (equivocator, _) =>
      val dictionary = equivocator.makeDictionary(ngrams)
      dictionary("b" ->"c") shouldBe Vector("this")
    }
    "take ngrams and turn them into a dictionary where values are accumulated by key" in withMocks { (equivocator, _) =>
      val dictionary = equivocator.makeDictionary(ngrams)
      dictionary("this" ->"is") shouldBe Vector("a", "z")
    }
  }
  "shufflePick" should {
    "pick a next word when a target is present" in withMocks { (equivocator, random) =>
      val listOfCandidates = Vector("c", "c", "d", "z")
      val dictionary = MutableMap(("a", "b") -> listOfCandidates)
      when(random.shuffle(org.mockito.ArgumentMatchers.eq(listOfCandidates))(org.mockito.ArgumentMatchers.any[CanBuildFrom[Vector[String],String,Vector[String]]])).thenReturn(Vector("d", "c", "c", "z"))

      equivocator.shufflePick(("a","b"), dictionary) shouldBe "d"
    }
    "pick a new target when a candidate can not be found" in withMocks { (equivocator, random) =>
      val listOfCandidates = Vector("c", "d", "z")
      val dictionary = MutableMap(("a", "b") -> listOfCandidates, ("b", "7") -> Vector())
      when(random.shuffle(org.mockito.ArgumentMatchers.eq(dictionary.keys))
        (org.mockito.ArgumentMatchers.any[CanBuildFrom[Iterable[(String, String)],(String, String),Iterable[(String, String)]]]))
        .thenReturn(Vector(("a","b")))
      equivocator.shufflePick(("NotAKey", "InTheMap"), dictionary) shouldBe "a"
    }
  }

}
