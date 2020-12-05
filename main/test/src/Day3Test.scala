package alexcardell.advent20

import cats.effect.testing.utest.IOTestSuite
import utest._
import Day3._

object Day3IOTest extends IOTestSuite {
  val tests = Tests {
    test("Part 1") {
      test("works for example") {
        run("day-3-test.txt", part1Gradients).map(_ ==> 7)
      }

      test("works for real input") {
        part1.map(_ ==> 282)
      }
    }

    test("Part 2") {
      test("works for example") {
        run("day-3-test.txt", part2Gradients).map(_ ==> 336)
      }

      test("works for real input") {
        part2.map(_ ==> 958815792)
      }
    }
  }
}
