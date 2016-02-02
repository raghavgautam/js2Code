import java.util

import scala.collection.JavaConverters._
import scala.collection.immutable.{Iterable, WrappedString}
import scala.collection.mutable

case class Cls(name: WrappedString, fields: Iterable[Field]) {
  val body: WrappedString = if (fields.nonEmpty) fields.map(_.toString).reduce(_ + _) else ""

  override def toString: String = {
    Template.render(Template.classTemplate, Map("class" -> this))
  }

  def getFields: util.List[Field] = fields.toList.asJava
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
    scala.util.parsing.json.JSON.parseFull(jsonStr).get match {
      case parsedJson: Map[String, Any] => new Js2Java(parsedJson, clsName).generate
      case _ => throw new RuntimeException("Invalid JSON Object.")
    }

  }

  private class Js2Java(parsedJson: Map[String, Any], className: String) {
    val classes = mutable.ListBuffer[Cls]()

    def underscoreToCamel(name: String) = "_([a-z\\d])".r.replaceAllIn(name, {m =>
      m.group(1).toUpperCase()
    })

    private def parseArr(className: String, parsed: Any): Cls = {
      parsed match {
        case propsArr: Seq[Any] =>
          if (propsArr.isEmpty)
            Cls(s"OneOf$className", List())
          else {
            propsArr.head match {
              case map: Map[String, Any] => Cls(s"OneOf$className", genFields(map))
              case basic: Any => Cls(s"OneOf$className", List(Field(basic.getClass.getSimpleName, basic.getClass.getSimpleName)))
            }
          }
        case _ => throw new Exception(parsed.toString)
      }
    }
    private def getType(kv: (String, Any)): String = {
      def sanitizeKey(key: String): String = underscoreToCamel(key.replace("-", "_").replace(".", "_").replace(":", "_"))
      val key: String = sanitizeKey(kv._1)
      val value: Any = kv._2
      value match {
        case s: Seq[Any] =>
          val cls = parseArr(key.capitalize, value)
          classes.append(cls)
          cls.name
        case s: Map[String, Any] =>
          val cls = Cls(key.capitalize, genFields(s))
          classes.append(cls)
          cls.name
        case _ =>
          if (value == null) "Unknown"
          else value.getClass.getSimpleName
      }
    }
    private def genFields(props: Map[String, Any]): Iterable[Field] = {
      props.map { kv =>
        val origName: String = kv._1
        val typ: Any = getType(kv)
        Field(origName, typ.toString)
      }
    }

    def generate = {
      Cls(className, genFields(parsedJson))
      classes.reverse.toList
    }
  }
}
