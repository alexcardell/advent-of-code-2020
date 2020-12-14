package alexcardell.advent20

import cats.effect.IO

object Day6 extends Day[Int, Int] {
  def run(path: String) =
    load(path).use(buf => {
      val lines = buf.getLines().toList
      val groups = groupLines(lines)

      val answersPerGroup = groups
        .map(groupAnswers =>
          for {
            personAnswers <- groupAnswers.toSet[String]
            chars <- personAnswers.toSet[Char]
          } yield chars
        )

      val part1Total = answersPerGroup.map(_.size).sum

      val part2Total = groups
        .map(groupAnswers =>
          groupAnswers
            .foldLeft(Map[Char, Int]())((groupAcc, personAnswers) => {
              personAnswers.foldLeft(groupAcc)((personAcc, answer) => {
                personAcc.updated(answer, personAcc.getOrElse(answer, 0) + 1)
              })
            })
            .filter {
              case (_, count) => count == groupAnswers.size
            }
            .size
        )
        .sum

      IO.pure((part1Total, part2Total))
    })

  def part1: IO[Int] = run("day-6-input.txt").map(_._1)
  def part2: IO[Int] = run("day-6-input.txt").map(_._2)
}
