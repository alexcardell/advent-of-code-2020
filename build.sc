import $ivy.`com.goyeau::mill-scalafix:0.1.5`
import com.goyeau.mill.scalafix.ScalafixModule

import mill._, scalalib._, modules._, scalafmt._

object main extends CommonModule

trait CommonModule extends ScalaModule with Quality {
  def scalaVersion = "2.13.1"

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

  object benchmarks extends Jmh with Quality {
    def scalaVersion = "2.13.1"
  }
}

trait Quality extends ScalafixModule with ScalafmtModule {
  def scalacOptions = Seq("-Ywarn-unused", "-deprecation")

  def scalafixIvyDeps = Agg(ivy"com.github.liancheng::organize-imports:0.4.0")
}

trait Jmh extends ScalaModule {

  def ivyDeps = super.ivyDeps() ++ Agg(ivy"org.openjdk.jmh:jmh-core:1.19")

  def runJmh(args: String*) =
    T.command {
      val (_, resources) = generateBenchmarkSources()
      Jvm.runSubprocess(
        "org.openjdk.jmh.Main",
        classPath = (runClasspath() ++ generatorDeps()).map(_.path) ++
          Seq(compileGeneratedSources().path, resources),
        mainArgs = args,
        workingDir = T.ctx.dest
      )
    }

  def compileGeneratedSources =
    T {
      val dest = T.ctx.dest
      val (sourcesDir, _) = generateBenchmarkSources()
      val sources = os.walk(sourcesDir).filter(os.isFile)
      os.proc(
        "javac",
        sources.map(_.toString),
        "-cp",
        (runClasspath() ++ generatorDeps()).map(_.path.toString).mkString(":"),
        "-d",
        dest
      ).call(dest)
      PathRef(dest)
    }

  // returns sources and resources directories
  def generateBenchmarkSources =
    T {
      val dest = T.ctx.dest

      val sourcesDir = dest / 'jmh_sources
      val resourcesDir = dest / 'jmh_resources

      os.remove.all(sourcesDir)
      os.makeDir.all(sourcesDir)
      os.remove.all(resourcesDir)
      os.makeDir.all(resourcesDir)

      Jvm.runSubprocess(
        "org.openjdk.jmh.generators.bytecode.JmhBytecodeGenerator",
        (runClasspath() ++ generatorDeps()).map(_.path),
        mainArgs = Array(
          compile().classes.path,
          sourcesDir,
          resourcesDir,
          "default"
        ).map(_.toString)
      )

      (sourcesDir, resourcesDir)
    }

  def generatorDeps =
    resolveDeps(
      T { Agg(ivy"org.openjdk.jmh:jmh-generator-bytecode:1.19") }
    )
}
