package alexcardell.advent20

import cats.effect.testing.utest.IOTestSuite
import utest._

object Day2IOTest extends IOTestSuite {
  val tests = Tests {
    test("Day 2") {
      test("Part 1") {
        test("returns correct value for example") {
          Day2
            .run("day-2-test.txt", Day2.Policy.isValidAsRange)
            .map(_ ==> 2)
        }

        test("returns correct value for part 1 input") {
          Day2.part1.map(_ ==> 580)
        }
      }

      test("Part 2") {
        test("returns correct value for example") {
          Day2.run("day-2-test.txt", Day2.Policy.isValidAsPositions).map(_ ==> 1)
        }

        test("returns correct value for real input") {
          Day2.part2.map(_ ==> 611)
        }
      }
    }
  }
}
