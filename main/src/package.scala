package alexcardell

import cats.effect._

package object advent20 {
  trait Day[A, B] {
    def part1: IO[A]
    def part2: IO[B]
  }
}
