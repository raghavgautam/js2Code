object Main extends App {
  val data =
    """
      |{"menu": {
      |  "id": "file",
      |  "value": "File",
      |  "popup": {
      |    "menuitem": [
      |      {"value": "New", "onclick": "CreateNewDoc()"},
      |      {"value": "Open", "onclick": "OpenDoc()"},
      |      {"value": "Close", "onclick": "CloseDoc()"}
      |    ]
      |  }
      |}}
    """.stripMargin
  args.length match {
    case 0 =>
      println("No arg provided. Using demo data...")
      println(data)
      println(js2Java.generate(data, "JsRoot"))
      println("Please supply a json file name or url as argument.")
    case 1 =>
      val arg: String = args.head
      if (arg.startsWith("http://") || arg.startsWith("https://")) {
        val data = io.Source.fromURL(arg).mkString
        printJava(js2Java.generate(data, "JsRoot"), s"Invalid json fetched from url: $arg\n${data.take(500)}")
      } else {
        println("Need to handle this case.")
      }

  }

  def printJava(java: List[(String, String)], message: String): Unit = {
    try {
      java.map(_._2).foreach(println)
    } catch {
      case ex: Throwable => println(message)
    }
  }
}
