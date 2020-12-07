package alexcardell.advent20

import alexcardell.advent20.Day3._
import cats.effect.testing.utest.IOTestSuite
import utest._

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
