import scala.collection.mutable

object js2Java {
  def generate(jsonStr: String, clsName: String) = {
    new Js2Java(jsonStr, clsName).generate
  }

  private class Js2Java(json: String, className: String) {
    val classes = mutable.ListBuffer[(String, String)]()

    private def getType(kv: (String, Any)): String = {
      val key: String = kv._1
      val value: Any = kv._2
      value match {
        case s: Seq[Any] =>
          parseArrOrMap(value, key.capitalize)
        case s: Map[String, Any] =>
          parseArrOrMap(value, key.capitalize)
        case _ => value.getClass.getSimpleName
      }
    }
    private def sanitizeFieldName(name: String): String = {
      val x = mutable.StringBuilder.newBuilder
      x += name.head
      val partName = for(i <- 1 until name.length; prev = name(i - 1); curr = name(i)) yield {
        if (Set('-', '_').contains(prev))
          curr.toUpper
        else
          curr
      }
      x ++= partName
      x.toString().filter(_.isLetterOrDigit)
    }
    private def genFields(props: Map[String, Any]): Iterable[String] = {
      props.map { kv =>
        val origName: String = kv._1
        val fieldName: String = sanitizeFieldName(origName)
        val typ: Any = getType(kv)
        val fieldHeaderTemplate = "@SerializedName(\"%s\")"
        val fieldTemplate: String = "final %s %s;"
        fieldHeaderTemplate.format(origName) + "\n" +
          fieldTemplate.format(typ, fieldName) + "\n"
      }
    }
    private def genClass(props: Map[String, Any], className: String): String = {
      val classStart: String = s"class $className {"
      val classBody: String = genFields(props).reduce(_ + "\n" + _)
      val classEnd: String = "}"
      s"$classStart\n$classBody$classEnd\n"
    }

    def parseArrOrMap(parsed: Any, className: String): String = {
      val clsNameDef: (String, String) = parsed match {
        case props: Map[String, Any] => (className, genClass(props, className))
        case propsArr: Seq[Map[String, Any]] => (s"oneOf$className[]", genClass(propsArr.head, s"oneOf$className"))
        case _ => throw new Exception(parsed.toString)
      }
      classes.append(clsNameDef)
      clsNameDef._1
    }

    def generate: List[(String, String)] = {
      val parsed: Any = scala.util.parsing.json.JSON.parseFull(json).get
      parseArrOrMap(parsed, className)
      classes.reverse.toList
    }
  }
}
