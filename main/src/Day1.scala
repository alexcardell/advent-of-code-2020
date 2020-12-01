package alexcardell.advent20

import cats.effect.IO
import cats.effect.Resource
import scala.io.Source

object Day1 extends Day[Int, Int] {
  def run(path: String, n: Int = 2): IO[Int] =
    Resource
      .fromAutoCloseable(IO(Source.fromResource(path)))
      .map(_.getLines.to(List).map(_.toInt))
      .use(lines =>
        IO { lines.combinations(n).find(_.sum == 2020).map(_.product).head }
      )

  def part1: IO[Int] = run("day-1-input.txt", 2)

  def part2: IO[Int] = run("day-1-input.txt", 3)
}
