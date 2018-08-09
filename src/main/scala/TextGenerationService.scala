import monix.eval.Task
import org.http4s.{HttpService}
import org.http4s.dsl.Http4sDsl

class TextGenerationService(equivocator: Equivocate) extends Http4sDsl[Task]{
  val service: HttpService[Task] =  HttpService[Task]{
    case GET -> Root / "equivocate" =>
      equivocator.run().attempt.flatMap{ (either: Either[Throwable, String]) =>
        either match {
        case Left(_) => InternalServerError()
        case Right(statement) => Ok(statement)
      }
    }
  }
}
