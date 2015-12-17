import scala.collection.immutable.{Iterable, WrappedString}
import scala.collection.mutable

case class Cls(name: WrappedString, fields: Iterable[Field]) {
  val body: WrappedString = fields.map(_.toString).reduce(_ + _)

  override def toString: String = {
    Template.render(Template.classTemplate, Map("class" -> this))
  }
}
case class Field(origName: WrappedString, `type`: WrappedString) {
  val name = sanitizeFieldName(origName)
  private def sanitizeFieldName(name: WrappedString): WrappedString = {
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
    Template.render(Template.fieldTemplate, Map("field" -> this))
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
      cls.name
    }

    def generate = {
      parseArrOrMap(parsedJson, className)
      classes.reverse.toList
    }
  }
}
