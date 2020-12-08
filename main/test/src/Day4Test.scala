package alexcardell.advent20

import alexcardell.advent20.Day4._
import alexcardell.advent20.Day4.validation._
import cats.effect.testing.utest.IOTestSuite
import utest._
import cats.data._
import cats.data.Validated._

object Day4Test extends TestSuite {
  val tests = Tests {
    test("group lines works") {
      val input = List("a", "b", "", "c", "", "d")
      val expected = LazyList(LazyList("d"), LazyList("c"), LazyList("b", "a"))
      groupLines(input) ==> expected
    }

    test("validation") {
      def valid(v: String) = Valid(v)
      def invalidAux(field: String)(v: String) =
        Invalid(NonEmptyChain(InvalidField(field, v)))

      test("byr") {
        def invalid = invalidAux("byr")(_)

        test("is valid on lower range bound") {
          val input = 1920.toString
          validation.byr(input) ==> valid(input)
        }

        test("is invalid below lower range bound") {
          val input = 1919.toString
          validation.byr(input) ==> invalid(input)
        }

        test("is valid on upper range bound") {
          val input = 2002.toString
          validation.byr(input) ==> valid(input)
        }

        test("is invalid above upper range bound") {
          val input = 2003.toString
          validation.byr(input) ==> invalid(input)
        }

        test("is invalid for non integer input string") {
          val input = "2.3"
          validation.byr(input) ==> invalid(input)
        }

        test("is invalid for non numeric input string") {
          val input = "hello"
          validation.byr(input) ==> invalid(input)
        }
      }

      test("iyr") {
        def invalid = invalidAux("iyr")(_)

        test("is valid on lower range bound") {
          val input = 2010.toString
          validation.iyr(input) ==> valid(input)
        }

        test("is invalid below lower range bound") {
          val input = 2009.toString
          validation.iyr(input) ==> invalid(input)
        }

        test("is valid on upper range bound") {
          val input = 2020.toString
          validation.iyr(input) ==> valid(input)
        }

        test("is invalid above upper range bound") {
          val input = 2021.toString
          validation.iyr(input) ==> invalid(input)
        }

        test("is invalid for non integer input string") {
          val input = "2.3"
          validation.iyr(input) ==> invalid(input)
        }

        test("is invalid for non numeric input string") {
          val input = "hello"
          validation.iyr(input) ==> invalid(input)
        }
      }

      test("eyr") {
        def invalid = invalidAux("eyr")(_)

        test("is valid on lower range bound") {
          val input = 2020.toString
          validation.eyr(input) ==> valid(input)
        }

        test("is invalid below lower range bound") {
          val input = 2019.toString
          validation.eyr(input) ==> invalid(input)
        }

        test("is valid on upper range bound") {
          val input = 2030.toString
          validation.eyr(input) ==> valid(input)
        }

        test("is invalid above upper range bound") {
          val input = 2031.toString
          validation.eyr(input) ==> invalid(input)
        }

        test("is invalid for non integer input string") {
          val input = "2.3"
          validation.eyr(input) ==> invalid(input)
        }

        test("is invalid for non numeric input string") {
          val input = "hello"
          validation.eyr(input) ==> invalid(input)
        }
      }

      test("hgt") {
        def invalid = invalidAux("hgt")(_)

        test("is valid for `cm` suffix lower bound") {
          val input = "150cm"
          validation.hgt(input) ==> valid(input)
        }

        test("is valid for `cm` suffix upper bound") {
          val input = "193cm"
          validation.hgt(input) ==> valid(input)
        }

        test("is invalid for `cm` suffix above upper bound") {
          val input = "194cm"
          validation.hgt(input) ==> invalid(input)
        }

        test("is invalid for `cm` suffix below upper bound") {
          val input = "149cm"
          validation.hgt(input) ==> invalid(input)
        }

        test("is valid for `in` suffix lower bound") {
          val input = "59in"
          validation.hgt(input) ==> valid(input)
        }

        test("is valid for `in` suffix upper bound") {
          val input = "76in"
          validation.hgt(input) ==> valid(input)
        }

        test("is invalid for `in` suffix above upper bound") {
          val input = "77in"
          validation.hgt(input) ==> invalid(input)
        }

        test("is invalid for `in` suffix below upper bound") {
          val input = "58in"
          validation.hgt(input) ==> invalid(input)
        }

        test("is invalid for no suffix") {
          val input = "60"
          validation.hgt(input) ==> invalid(input)
        }

        test("is invalid for bad suffix") {
          val input = "60ab"
          validation.hgt(input) ==> invalid(input)
        }

        test("is invalid for alphabetic input") {
          val input = "ab"
          validation.hgt(input) ==> invalid(input)
        }
      }

      test("hcl") {
        def invalid = invalidAux("hcl")(_)

        test("is valid for all as") {
          val input = "#aaaaaa"
          validation.hcl(input) ==> valid(input)
        }

        test("is valid for all fs") {
          val input = "#ffffff"
          validation.hcl(input) ==> valid(input)
        }

        test("is valid for all 0s") {
          val input = "#000000"
          validation.hcl(input) ==> valid(input)
        }

        test("is valid for all 9s") {
          val input = "#999999"
          validation.hcl(input) ==> valid(input)
        }

        test("is valid for a valid hex") {
          val input = "#f0f0f0"
          validation.hcl(input) ==> valid(input)
        }

        test("is invalid without hash") {
          val input = "f0f0f0"
          validation.hcl(input) ==> invalid(input)
        }

        test("is invalid with hash at end") {
          val input = "f0f0f0#"
          validation.hcl(input) ==> invalid(input)
        }
      }

      test("pid") {
        def invalid = invalidAux("pid")(_)

        test("is valid for all 0") {
          val input = "000000000"
          validation.pid(input) ==> valid(input)
        }

        test("is valid for all 9") {
          val input = "999999999"
          validation.pid(input) ==> valid(input)
        }

        test("is invalid less than 9 digits") {
          val input = "00000000"
          validation.pid(input) ==> invalid(input)
        }

        test("is invalid more than 9 digits") {
          val input = "0000000000"
          validation.pid(input) ==> invalid(input)
        }
      }
    }
  }
}

object Day4IOTest extends IOTestSuite {
  private val testResource = "day-4-test.txt"

  val tests = Tests {
    test("Part 1") {
      test("works for test input") {
        run(testResource).map(_._1 ==> 2)
      }

      test("works for real input") {
        part1.map(_ ==> 182)
      }
    }

    test("Part 2") {
      test("works for real input") {
        part2.map(_ ==> 109)
      }
    }
  }
}
