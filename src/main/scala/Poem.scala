import monix.eval.{MVar, Task}
import better.files._

import scala.concurrent.Await
import scala.concurrent.duration.Duration

class Poem {
  val lock = new MLock()

  def read: Task[Seq[String]] = {
    Task(File("./data/TEST.txt").contentAsString.split("\n"))
  }

  def append(lineToAppend: String): Task[Unit] = {
    lock.greenLight(
      Task {
        println("Hey I'm writing to the file")
        File("./data/TEST.txt").createIfNotExists().appendLine(lineToAppend)
        Thread.sleep(5000)
      }
    )
  }
}

class MLock {
  private[this] val mvar: Task[MVar[Unit]] = MVar(())

  def acquire: Task[Unit] =
    mvar.map(_.take)

  def release: Task[Unit] =
    mvar.map(_.put(()))

  def greenLight[A](fa: Task[A]): Task[A] =
    for {
      _ <- acquire
      a <- fa.doOnCancel(release)
      _ <- release
    } yield a
}

object Test extends App {
  import monix.execution.Scheduler.Implicits.global
  val  p = new Poem

  val tasks = Seq(p.append("adding a new thing1"), p.append("adding a new thing2"), p.append("adding a new thing3"), p.append("adding a new thing4"), p.append("adding a new thing5"))

  Await.result(Task.gather[Unit, Seq](tasks).runAsync, Duration.Inf)
  Await.result(p.read.runAsync.map(x => x.foreach(println)), Duration.Inf)
}