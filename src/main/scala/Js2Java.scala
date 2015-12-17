import scala.collection.immutable.Iterable
import scala.collection.mutable

object js2Java {
  def generate(jsonStr: String, clsName: String) = {
    val parsedJson: Any = scala.util.parsing.json.JSON.parseFull(jsonStr).get
    new Js2Java(parsedJson, clsName).generate
  }

  private class Js2Java(parsedJson: Any, className: String) {
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
    case class Cls(clsName: String, fields: Iterable[Field])
    case class Field(origName: String, typ: String) {
      def fieldName: String = sanitizeFieldName(origName)
    }
    private def genFields(props: Map[String, Any]): Iterable[Field] = {
      props.map { kv =>
        val origName: String = kv._1
        val typ: Any = getType(kv)
        Field(origName, typ.toString)
      }
    }
    private def genCode(cls: Cls): String = {
      val classTemplate: String =
        """
          |class %s {
          |%s
          |}
        """.stripMargin
      val fieldTemplate: String =
        """
          |@SerializedName("%s")
          |final %s %s;
        """.stripMargin
      val classBody: String = cls.fields.map(f => {fieldTemplate.format(f.origName, f.typ, f.fieldName)}).reduce(_ + _)
      classTemplate.format(cls.clsName, classBody)
    }

    def parseArrOrMap(parsed: Any, className: String): String = {
      val clsNameDef: (String, String) = parsed match {
        case props: Map[String, Any] => (className, genCode(Cls(className, genFields(props))))
        case propsArr: Seq[Map[String, Any]] => (s"oneOf$className[]", genCode(Cls(s"oneOf$className", genFields(propsArr.head))))
        case _ => throw new Exception(parsed.toString)
      }
      classes.append(clsNameDef)
      clsNameDef._1
    }

    def generate: List[(String, String)] = {
      parseArrOrMap(parsedJson, className)
      classes.reverse.toList
    }
  }
}
