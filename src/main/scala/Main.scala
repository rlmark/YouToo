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

// ~~~!*)()@#(FAVES!*)()@#(~~~
//I am sorry for the truthiness of the truth is I am blessed to be untrue.
//I am not some innocent bystander I am in a position of significant power.
//I am confident that these women know I haven’t been sober for more than 5 years.
//I am cooperating with the House of Representatives through hyperbolized public excoriation.
//I am leaving while a man of violence.
//The future belongs to those who felt hurt
//The future belongs to those who felt wronged by me.
//I am not giving up my voice.
//I am writing this I realize that is not about me.
//I am part of our behavior.
// TODO: poem state storage
