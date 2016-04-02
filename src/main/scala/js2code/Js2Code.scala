/*
 * Copyright 2016 rgautam
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package js2code

import java.util

import scala.collection.JavaConverters._
import scala.collection.immutable.{Iterable, WrappedString}
import scala.collection.mutable

object Js2Code {
  def generate(jsonStr: String, clsName: String) = {
    scala.util.parsing.json.JSON.parseFull(jsonStr).get match {
      case parsedJson: Map[String, Any] => new Js2CodeInner(parsedJson, clsName).generate
      case _ => throw new RuntimeException("Invalid JSON Object.")
    }

  }

  private class Js2CodeInner(parsedJson: Map[String, Any], className: String) {
    val classes = mutable.ListBuffer[Cls]()

    def underscoreToCamel(name: String) = "_([a-z\\d])".r.replaceAllIn(name, {m =>
      m.group(1).toUpperCase()
    })

    private def parseArr(className: String, arr: Seq[Any]): Cls = {
      if (arr.isEmpty)
        Cls(s"OneOf$className", List())
      else {
        arr.head match {
          case mapArr: Map[String, Any] => Cls(s"OneOf$className", genFields(mapArr))
          case basicArr: Any => Cls(s"OneOf$className", List(Field(basicArr.getClass.getSimpleName, basicArr.getClass.getSimpleName)))
        }
      }
    }
    private def getType(kv: (String, Any)): (String, Boolean) = {
      def sanitizeKey(key: String): String = underscoreToCamel(key.replace("-", "_").replace(".", "_").replace(":", "_"))
      val key: String = sanitizeKey(kv._1)
      val value: Any = kv._2
      value match {
        case s: Seq[Any] =>
          val cls = parseArr(key.capitalize, s)
          classes.append(cls)
          (cls.name, true)
        case s: Map[String, Any] =>
          val cls = Cls(key.capitalize, genFields(s))
          classes.append(cls)
          (cls.name, false)
        case _ =>
          val typ =
            if (value == null) "Unknown"
            else value.getClass.getSimpleName
          (typ, false)
      }
    }
    private def genFields(props: Map[String, Any]): Iterable[Field] = {
      props.map { kv =>
        val origName: String = kv._1
        val typ = getType(kv)
        Field(origName, typ._1, typ._2)
      }
    }

    def generate = {
      classes.append(Cls(className, genFields(parsedJson)))
      classes.reverse.toList
    }
  }
}
