package alexcardell.advent20

import scala.io.Source
import scala.util.Try

import cats.data._
import cats.effect.IO
import cats.effect.Resource
import cats.implicits._

object Day4 extends Day[Int, Int] {
  object validation {
    trait VError
    case class InvalidField(k: String, v: String) extends VError
    case object MissingFields extends VError

    type Validation[A] = ValidatedNec[VError, A]

    private def yearInRange(range: Range.Inclusive, fieldName: String)(
        value: String
    ): Validation[String] =
      Try(value.toInt).toEither
        .leftMap(_ => NonEmptyChain(InvalidField(fieldName, value)))
        .toValidated
        .andThen(year => {
          if (range.contains(year)) year.toString.validNec
          else InvalidField(fieldName, value).invalidNec
        })

    def byr(value: String): Validation[String] =
      yearInRange(1920 to 2002, "byr")(value)

    def iyr(value: String): Validation[String] =
      yearInRange(2010 to 2020, "iyr")(value)

    def eyr(value: String): Validation[String] =
      yearInRange(2020 to 2030, "eyr")(value)

    def hgt(value: String): Validation[String] = {
      lazy val valid = value.validNec
      lazy val invalid = InvalidField("hgt", value).invalidNec

      val re = "([0-9]*)(cm|in)".r

      value match {
        case re(h, "cm") =>
          if ((150 to 193).contains(h.toInt)) valid
          else invalid
        case re(h, "in") =>
          if ((59 to 76).contains(h.toInt)) valid
          else invalid
        case _ =>
          invalid
      }
    }

    def hcl(value: String): Validation[String] = {
      val re = "(#[a-f0-9]{6})".r
      value match {
        case re(m) => m.validNec
        case _ => InvalidField("hcl", value).invalidNec
      }
    }

    def ecl(value: String): Validation[String] = {
      if (Set("amb", "blu", "brn", "gry", "grn", "hzl", "oth").contains(value))
        value.validNec
      else
        InvalidField("ecl", value).invalidNec
    }

    def pid(value: String): Validation[String] = {
      val re = "[0-9]{9}".r
      if (re.matches(value) && value.length == 9) value.validNec
      else InvalidField("pid", value).invalidNec
    }

    def cid(value: String): Validation[String] = value.validNec

    def validate(map: Map[String, String]): Validation[Passport] = {
      (
        map.get("byr"),
        map.get("iyr"),
        map.get("eyr"),
        map.get("hgt"),
        map.get("hcl"),
        map.get("ecl"),
        map.get("pid")
      ).tupled
        .map { p =>
          (
            byr(p._1),
            iyr(p._2),
            eyr(p._3),
            hgt(p._4),
            hcl(p._5),
            ecl(p._6),
            pid(p._7)
          ).mapN(Passport.apply)
        }
        .getOrElse(MissingFields.invalidNec)
    }
  }

  case class Passport(
      byr: String, // (Birth Year)
      iyr: String, // (Issue Year)
      eyr: String, // (Expiration Year)
      hgt: String, // (Height)
      hcl: String, // (Hair Color)
      ecl: String, // (Eye Color)
      pid: String // (Passport ID)
  )

  def groupLines(lines: Seq[String]): Seq[Seq[String]] = {
    val init = LazyList(LazyList[String]())

    lines.foldLeft(init)((acc, line) =>
      if (line == "") {
        acc.prepended(LazyList())
      } else {
        val newHead = acc.head.prepended(line)
        acc.tail.prepended(newHead)
      }
    )
  }

  def splitComponents(s: String): Map[String, String] =
    s.split(' ').map(_.split(':')).map(a => (a(0), a(1))).toMap

  def componentsArePresent(s: String): Option[Map[String, String]] = {
    val components = splitComponents(s)
    val valid =
      components.size == 8 || (components.size == 7 && !components.contains(
        "cid"
      ))
    Option.when(valid)(components)
  }

  def run(path: String): IO[(Int, Int)] =
    Resource
      .fromAutoCloseable(IO(Source.fromResource(path)))
      .use(source => {
        val lines = source.getLines().to(LazyList)
        val blocks = groupLines(lines).map(_.mkString(" "))
        val count = blocks.map(componentsArePresent).count(_.isDefined == true)

        val count2 =
          blocks
            .map(s =>
              s.split(' ').map(_.split(':')).map(a => (a(0), a(1))).toMap
            )
            .map(validation.validate)
            .count(_.isValid)

        IO.pure((count, count2))
      })

  def part1 = run("day-4-input.txt").map(_._1)
  def part2 = run("day-4-input.txt").map(_._2)
}
