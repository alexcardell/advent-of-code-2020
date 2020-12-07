package alexcardell.advent20

import scala.io.Source
import scala.util.Success
import scala.util.Try

import cats.effect.IO
import cats.effect.Resource
import cats.implicits._

object Day2 extends Day[Int, Int] {
  case class Policy(min: Int, max: Int, char: Char, pwd: String)

  object Policy {
    def from(str: String): Try[Policy] =
      Try {
        val components = str.split(' ').toVector

        val range = components(0).split('-').toVector
        val min = range(0).toInt
        val max = range(1).toInt
        val char = components(1).toCharArray().head
        val pwd = components(2)

        Policy(min, max, char, pwd)
      }

    def isValidAsRange(p: Policy): Boolean =
      (p.min to p.max).contains(p.pwd.count(_ == p.char))

    def isValidAsPositions(p: Policy): Boolean =
      Try {
        val first = p.pwd(p.min - 1) == p.char
        val second = p.pwd(p.max - 1) == p.char

        (first, second)
      } match {
        case Success((true, false)) | Success((false, true)) => true
        case _                                               => false
      }

  }

  def run(
      path: String,
      validationFn: Policy => Boolean
  ) =
    Resource
      .fromAutoCloseable(IO(Source.fromResource(path)))
      .use(source =>
        IO.fromTry {
          source.getLines().toVector
            .map(Policy.from)
            .sequence
            .map(policies => policies.map(validationFn).count(_ == true))
        }
      )

  def part1: IO[Int] = run("day-2-input.txt", Policy.isValidAsRange)

  def part2: IO[Int] = run("day-2-input.txt", Policy.isValidAsPositions)
}
