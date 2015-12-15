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
  println(new Js2Java(data).generate)
}
