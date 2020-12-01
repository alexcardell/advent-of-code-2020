import $ivy.`com.goyeau::mill-scalafix:0.1.5`
import com.goyeau.mill.scalafix.ScalafixModule

import mill._, scalalib._, scalafmt._

trait Quality extends ScalafixModule with ScalafmtModule {
  def scalacOptions = Seq("-Ywarn-unused")

  def scalafixIvyDeps = Agg(ivy"com.github.liancheng::organize-imports:0.4.0")
}

trait CommonModule extends ScalaModule with Quality {
  def scalaVersion = "2.13.1"

  def scalacOptions = Seq("-Ywarn-unused", "-deprecation")

  def ivyDeps =
    Agg(
      ivy"org.typelevel::cats-core:2.2.0",
      ivy"org.typelevel::cats-effect:2.2.0"
    )

  object test extends Tests with Quality {
    def ivyDeps =
      Agg(
        ivy"com.lihaoyi::utest:0.7.1",
        ivy"com.codecommit::cats-effect-testing-utest:0.4.2"
      )
    def testFrameworks = Seq("utest.runner.Framework")
  }
}

object main extends CommonModule
