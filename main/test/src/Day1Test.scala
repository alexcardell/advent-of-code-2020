package alexcardell.advent20

import cats.effect.testing.utest.IOTestSuite
import utest._

object Day1IOTest extends IOTestSuite {
  val tests = Tests {
    test("Part 1") {
      test("returns correct value for example") {
        Day1.run("day-1-test.txt").map(_ ==> 514579)
      }

      test("returns correct value for part 1 input") {
        Day1.part1.map(_ ==> 381699)
      }
    }

    test("Part 2") {
      test("returns correct value for example") {
        Day1.run("day-1-test.txt", 3).map(_ ==> 241861950)
      }

      test("returns correct value for real input") {
        Day1.part2.map(_ ==> 111605670)
      }
    }
  }
}
