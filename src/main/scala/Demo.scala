object Demo extends App {
  Template.setTemplates(Util.getResource("demo.vm"), null)
  val jsStr = io.Source.fromURL("https://api.github.com/users/raghavgautam").mkString
  val scalaCode = js2Code.generate(jsStr, "GithubUser").map(_.toString).reduce(_ + "\n" + _)
  println("*"*10 + " SCALA CODE " + "*"*10)
  println(scalaCode)
  println("*"*10 + " PYTHON CODE " + "*"*10)
  Template.setTemplates(Util.getResource("python-demo.vm"), null)
  val pythonCode = js2Code.generate(jsStr, "GithubUser").map(_.toString).reduce(_ + "\n" + _)
  print(pythonCode)
}
