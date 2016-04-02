object Demo extends App {
  Template.setTemplates(Util.getResource("template/demo.vm"))
  val jsStr = io.Source.fromURL("https://api.github.com/users/raghavgautam").mkString
  val scalaCode = Js2Code.generate(jsStr, "GithubUser").map(_.toString).reduce(_ + "\n" + _)
  println("*"*10 + " SCALA CODE " + "*"*10)
  println(scalaCode)
  println("*"*10 + " PYTHON CODE " + "*"*10)
  Template.setTemplates(Util.getResource("template/python.vm"))
  val pythonCode = Js2Code.generate(jsStr, "GithubUser").map(_.toString).reduce(_ + "\n" + _)
  print(pythonCode)
}
