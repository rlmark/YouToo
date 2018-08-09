import monix.eval.Task
import org.http4s.HttpService
import org.http4s.dsl.Http4sDsl

class PoemService(poemRepo: PoemRepository) extends Http4sDsl[Task]{
  val service: HttpService[Task] = HttpService[Task] {
    case GET -> Root / "poem" =>
      val t = poemRepo.read.map(_.mkString("\n"))

      Ok(t)
    case req @ POST -> Root / "poem" => ???
  }
}
