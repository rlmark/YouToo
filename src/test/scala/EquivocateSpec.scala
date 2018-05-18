import org.scalatest.mockito.MockitoSugar
import org.scalatest.{Matchers, WordSpec}

class EquivocateSpec extends WordSpec with Matchers with MockitoSugar {

  def withMocks(f: (Equivocate) => Unit): Unit = {
    val e = new Equivocate()
    f(e)
  }

  "tokenize" should {
    "split text into a Vector" in withMocks { equivocator =>
      val sampleString = "this is a test."
      equivocator.tokenize(sampleString) shouldBe Vector("this", "is", "a", "test", ".")
    }
    "strip punctuation except sentence endings" in withMocks { equivocator =>
      val sampleString = "hey, ();contains: punctuation()."
      equivocator.tokenize(sampleString) shouldBe Vector("hey", "contains", "punctuation", ".")
    }
  }
  "ngram" should {
    "take tokens and turn them into Trigrams" in withMocks { equivocator =>
      val sampleTokens = Vector("this", "is", "a", "test", ".")
      val expected = List(Vector("this", "is", "a"), Vector("is", "a", "test"), Vector("a", "test", "."))
      equivocator.ngram(sampleTokens).toList shouldBe expected
    }
  }
  "makeDictionary" should {
    val ngrams = Iterator(
      Vector("this", "is", "a"),
      Vector("is", "a", "test"),
      Vector("a", "test", "b"),
      Vector("test", "b", "c"),
      Vector("b","c","this"),
      Vector("c","this","is"),
      Vector("this","is","z"))
    "take ngrams and turn them into a dictionary with appropriate keys" in withMocks { equivocator =>
      val dictionary = equivocator.makeDictionary(ngrams)
      dictionary.keys.toSet shouldBe Set("this" ->"is", "is" -> "a", "a" -> "test", "test" -> "b", "b"->"c", "c"->"this")
    }
    "take ngrams and turn them into a dictionary that handles single values for a key" in withMocks { equivocator =>
      val dictionary = equivocator.makeDictionary(ngrams)
      dictionary foreach println
      dictionary("b" ->"c") shouldBe Vector("this")
    }
    "take ngrams and turn them into a dictionary where values are accumulated by key" in withMocks { equivocator =>
      val dictionary = equivocator.makeDictionary(ngrams)
      dictionary("this" ->"is") shouldBe Vector("a", "z")
    }

  }
}
