package alexcardell.advent20

import cats.effect.IO
import cats.effect.Resource
import scala.io.Source

object Day3 extends Day[Int, Int] {
  case class Gradient(right: Int, down: Int)

  case class Path(
      gradient: Gradient,
      treesHit: Int = 0,
      currentPosition: Int = 0
  )

  val part1Gradients = Array(Gradient(3, 1))
  val part2Gradients = Array(
    Gradient(1, 1),
    Gradient(3, 1),
    Gradient(5, 1),
    Gradient(7, 1),
    Gradient(1, 2)
  )

  def run(path: String, gradients: Array[Gradient]): IO[Int] =
    Resource
      .fromAutoCloseable(IO(Source.fromResource(path)))
      .map(source => source.getLines.to(LazyList).zipWithIndex)
      .use(lines => {
        val paths = gradients.map(g => Path(gradient = g))

        val treesHit =
          lines.tail
            .foldLeft(paths) {
              case (acc, (line, depth)) =>
                acc.map { path =>
                  val correctDepth = depth % path.gradient.down == 0
                  val newPos =
                    (path.currentPosition + path.gradient.right) % line.length
                  val hitTree = line(newPos) == '#'

                  (correctDepth, hitTree) match {
                    case (true, true) =>
                      path.copy(
                        treesHit = path.treesHit + 1,
                        currentPosition = newPos
                      )
                    case (true, _) =>
                      path.copy(
                        currentPosition = newPos
                      )
                    case _ => path
                  }
                }
            }
            .map(_.treesHit)
            .product

        IO.pure(treesHit)
      })

  def part1: IO[Int] = run("day-3-input.txt", part1Gradients)
  def part2: IO[Int] = run("day-3-input.txt", part2Gradients)
}
