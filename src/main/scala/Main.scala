import fs2.StreamApp.ExitCode
import fs2.{Stream, StreamApp}
import monix.eval.Task
import monix.execution.Scheduler.Implicits.global
import org.http4s.HttpService
import org.http4s.server.blaze.BlazeBuilder

import scala.util.Random

object Main extends StreamApp[Task] {
  val random = new Random()
  val equivocator = new Equivocate(random)

  val services: HttpService[Task] = new TextGenerationService(equivocator).service

  override def stream(args: List[String], requestShutdown: Task[Unit]): Stream[Task, ExitCode] =
    BlazeBuilder[Task]
      .bindHttp(8080, "localhost")
      .mountService(services, "/")
      .serve
}