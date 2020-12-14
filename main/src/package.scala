package alexcardell

import cats.effect._
import scala.io.Source

package object advent20 {
  trait Day[A, B] {
    def part1: IO[A]

    def part2: IO[B]
  }

  def load(path: String) =
    Resource.fromAutoCloseable(IO(Source.fromResource(path)))

  def groupLines(lines: Seq[String]): Seq[Seq[String]] = {
    val init = Seq(Seq[String]())

    lines.foldLeft(init)((acc, line) =>
      if (line == "") {
        acc.prepended(Seq())
      } else {
        val newHead = acc.head.prepended(line)
        acc.tail.prepended(newHead)
      }
    )
  }

}
