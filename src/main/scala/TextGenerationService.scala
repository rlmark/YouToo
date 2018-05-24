import monix.eval.Task
import org.http4s.HttpService
import org.http4s.dsl.Http4sDsl

class TextGenerationService(equivocator:Equivocate) extends Http4sDsl[Task]{
  val service: HttpService[Task] =  HttpService[Task]{
    case GET -> Root / "equivocate" =>
      Ok(equivocator.run())
  }
}
