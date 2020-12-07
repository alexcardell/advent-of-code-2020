package alexcardell.advent20

import alexcardell.advent20.Day2._
import cats.effect.testing.utest.IOTestSuite
import utest._

object Day2IOTest extends IOTestSuite {
  val tests = Tests {
    test("Part 1") {
      test("returns correct value for example") {
        Day2
          .run("day-2-test.txt", Policy.isValidAsRange)
          .map(_ ==> 2)
      }

      test("returns correct value for part 1 input") {
        part1.map(_ ==> 580)
      }
    }

    test("Part 2") {
      test("returns correct value for example") {
        run("day-2-test.txt", Policy.isValidAsPositions).map(_ ==> 1)
      }

      test("returns correct value for real input") {
        part2.map(_ ==> 611)
      }
    }
  }
}
