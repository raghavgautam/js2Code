import scala.collection.mutable

class Js2Java(json: String) {
  val classes = mutable.ListBuffer[String]()

  private def getType(kv: (String, Any)): String = {
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
  private def genFields(props: Map[String, Any]): Iterable[String] = {
    props.map { kv =>
      val fieldName: String = kv._1
      val typ: Any = getType(kv)
      val fieldHeaderTemplate = "@SerializedName(\"%s\")"
      val fieldTemplate: String = "final %s %s;"
      fieldHeaderTemplate.format(fieldName) + "\n" +
        fieldTemplate.format(typ, fieldName) + "\n"
    }
  }
  private def genClass(props: Map[String, Any], className: String): String = {
    val classStart: String = s"class $className {"
    val classBody: String = genFields(props).reduce(_ + "\n" + _)
    val classEnd: String = "}"
    s"$classStart\n$classBody$classEnd\n"
  }

  def parseJsValue(parsed: Any, className: String): String = {
    val clsNtyp: (String, String) = parsed match {
      case props: Map[String, Any] => (genClass(props, className), className)
      case propsArr: Array[Map[String, Any]] => (genClass(propsArr.head, s"oneOf$className"), s"oneOf$className[]")
    }
    classes.append(clsNtyp._1)
    clsNtyp._2
  }

  def generate: List[String] = {
    val parsed: Any = scala.util.parsing.json.JSON.parseFull(json).get
    parseJsValue(parsed, "JsRoot")
    classes.toList
  }
}
