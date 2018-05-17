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
}
