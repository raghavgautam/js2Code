object Demo extends App {
  Template.setTemplates(Util.getResource("demo.vm"), null)
  val jsStr = io.Source.fromURL("https://api.github.com/users/raghavgautam").mkString
  val scalaStr = js2Code.generate(jsStr, "GithubUser").map(_.toString).reduce(_ + "\n" + _)
  print(scalaStr)
}
