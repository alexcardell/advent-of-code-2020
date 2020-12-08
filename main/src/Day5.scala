package alexcardell.advent20

import cats.effect.IO
import cats.effect.Resource
import scala.io.Source

object Day5 extends Day[Int, Int] {
  implicit class TupleOps(tup: (Int, Int)) { self =>
    def eq() = tup._1 == tup._2
    def mid() = (tup._1 + tup._2) / 2
    def lower() = (tup._1, tup.mid())
    def upper() = (tup.mid() + 1, tup._2)
  }

  def seatId(input: String): Int = {
    @annotation.tailrec
    def aux(s: String, row: (Int, Int), col: (Int, Int)): Int =
      s.headOption match {
        case Some('F')                    => aux(s.tail, row.lower(), col)
        case Some('B')                    => aux(s.tail, row.upper(), col)
        case Some('L')                    => aux(s.tail, row, col.lower())
        case Some('R')                    => aux(s.tail, row, col.upper())
        case None if row.eq() && col.eq() => row._1 * 8 + col._1
        case ex =>
          throw new IllegalArgumentException(s"Bad value: ${ex}")
      }

    aux(input, (0, 127), (0, 7))
  }

  @annotation.tailrec
  def findMissing(ids: Seq[Int]): Int =
    ids match {
      case h :: i :: _ if i - h == 2 => h + 1
      case _ :: t                    => findMissing(t)
    }

  def run(path: String) =
    Resource
      .fromAutoCloseable(IO(Source.fromResource(path)))
      .use(source => {
        val seatIds = source.getLines().to(List).map(seatId)

        val maxSeatId = seatIds.max
        val mySeatId = findMissing(seatIds.sorted)

        IO.pure((maxSeatId, mySeatId))
      })

  def part1: IO[Int] = run("day-5-input.txt").map(_._1)
  def part2: IO[Int] = run("day-5-input.txt").map(_._2)
}
