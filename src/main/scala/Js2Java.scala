import scala.collection.immutable.Iterable
import scala.collection.mutable

case class Cls(clsName: String, fields: Iterable[Field]) {
  override def toString: String = {
    val classTemplate: String =
      """
        |class $className {
        |$fields
        |}
      """.stripMargin
    val classBody: String = fields.map(_.toString).reduce(_ + _)
    TemplateRenderer.render(classTemplate, Map("className" -> clsName, "fields" -> classBody))
  }
}
case class Field(origName: String, typ: String) {
  def fieldName: String = sanitizeFieldName(origName)
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

  override def toString = {
    val fieldTemplate: String =
      """
        |    @SerializedName("$originalFieldName")
        |    final $fieldType $fieldName;
      """.stripMargin
    TemplateRenderer.render(fieldTemplate, Map("originalFieldName" -> origName, "fieldType" -> typ, "fieldName" -> fieldName))
  }
}

object js2Java {
  def generate(jsonStr: String, clsName: String) = {
    val parsedJson: Any = scala.util.parsing.json.JSON.parseFull(jsonStr).get
    new Js2Java(parsedJson, clsName).generate
  }

  private class Js2Java(parsedJson: Any, className: String) {
    val classes = mutable.ListBuffer[Cls]()

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
    private def genFields(props: Map[String, Any]): Iterable[Field] = {
      props.map { kv =>
        val origName: String = kv._1
        val typ: Any = getType(kv)
        Field(origName, typ.toString)
      }
    }
    def parseArrOrMap(parsed: Any, className: String): String = {
      val cls = parsed match {
        case props: Map[String, Any] => Cls(className, genFields(props))
        case propsArr: Seq[Map[String, Any]] => Cls(s"oneOf$className", genFields(propsArr.head))
        case _ => throw new Exception(parsed.toString)
      }
      classes.append(cls)
      cls.clsName
    }

    def generate = {
      parseArrOrMap(parsedJson, className)
      classes.reverse.toList
    }
  }
}
