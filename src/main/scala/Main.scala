import js2code.{Cls, Js2Code, Util}

object Main extends App {
  val data = Util.getResource("/json/sample.json")
  val javaTemplate = Util.getResource("/template/java.vm")
  args.length match {
    case 0 =>
      println("No arg provided. Using demo data...")
      println(data)
      printJava(Js2Code.generate(data, "JsRoot"), s"Invalid json.\n${data.take(500)}")
      println("Please supply a json file name or url as argument.")
    case 1 =>
      val arg: String = args.head
      if (arg.startsWith("http://") || arg.startsWith("https://")) {
        val data = io.Source.fromURL(arg).mkString
        printJava(Js2Code.generate(data, "JsRoot"), s"Invalid json fetched from url: $arg\n${data.take(500)}")
      } else {
        println("Need to handle this case.")
      }

  }

  def printJava(java: List[Cls], message: String): Unit = {
    try {
      java.foreach(i => println(i.renderCode(javaTemplate)))
    } catch {
      case ex: Throwable => println(message)
    }
  }
}
