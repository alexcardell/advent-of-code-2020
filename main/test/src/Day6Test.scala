package alexcardell.advent20

import cats.effect.testing.utest.IOTestSuite
import utest._
import Day6._

object Day6IOTest extends IOTestSuite {
  val tests = Tests {
    test("test part 1") {
      run("day-6-test.txt").map(_._1 ==> 11)
    }

    test("run part 1") {
      part1.map(_ ==> 6763)
    }

    test("test part 2") {
      run("day-6-test.txt").map(_._2 ==> 6)
    }

    test("run part 2") {
      part2.map(_ ==> 3512)
    }
  }
}
