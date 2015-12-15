object Main extends App {
  def getType(kv: (String, Any)): String = {
    val key: String = kv._1
    val value: Any = kv._2
    value match {
      case s: Seq[Any] =>
        val className = "SomeClass"
        s"$className[]"
      case s: Map[String, Any] =>
        val className = key.capitalize
        genClass(s, className)
        s"$className"
      case _ => value.getClass.getSimpleName
    }
  }
  def genFields(props: Map[String, Any]): Iterable[String] = {
    props.map { kv =>
      val fieldName: String = kv._1
      val typ: Any = getType(kv)
      val fieldHeaderTemplate = "@SerializedName(\"%s\")"
      val fieldTemplate: String = "final %s %s;"
      fieldHeaderTemplate.format(fieldName) + "\n" +
        fieldTemplate.format(typ, fieldName) + "\n"
    }
  }
  def genClass(props: Map[String, Any], className: String = "JsRoot"): String = {
    val classStart: String = s"class $className {"
    val classBody: String = genFields(props).reduce(_ + "\n" + _)
    val classEnd: String = "}"
    s"$classStart\n$classBody$classEnd\n"
  }
  def getJava(data: String): List[String] = {
    val parsed: Option[Any] = scala.util.parsing.json.JSON.parseFull(data)
    val props = parsed.get.asInstanceOf[Map[String, Any]]
    List(genClass(props))
  }

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
  println(getJava(data))
}
