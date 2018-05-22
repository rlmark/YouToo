import monix.eval.Task
import org.http4s.dsl.Http4sDsl

class TextGenerationService extends Http4sDsl[Task]{
  val service = ???
}
