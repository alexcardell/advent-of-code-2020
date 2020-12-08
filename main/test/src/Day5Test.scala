package alexcardell.advent20

import cats.effect.testing.utest.IOTestSuite
import utest._
import Day5._

object Day5Test extends TestSuite {
  val tests = Tests {
    test("seatId") {
      test("example 1") {
        val input = "FBFBBFFRLR"
        seatId(input) ==> 357
      }

      test("example 2") {
        val input = "BFFFBBFRRR"
        seatId(input) ==> 567
      }

      test("example 3") {
        val input = "FFFBBBFRRR"
        seatId(input) ==> 119
      }

      test("example 4") {
        val input = "BBFFBBFRLL"
        seatId(input) ==> 820
      }
    }
  }
}

object Day5IOTest extends IOTestSuite {
  val tests = Tests {
    test("run part 1") {
      part1.map(_ ==> 883)
    }

    test("run part 2") {
      part2.map(_ ==> 532)
    }
  }
}
