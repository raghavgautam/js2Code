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

import scala.collection.immutable.WrappedString
import scala.collection.JavaConverters._
/**
  * Created by rgautam on 4/1/16.
  */
case class Cls(name: WrappedString, fields: Iterable[Field]) {
  override def toString(): String = {
    throw new RuntimeException("should not get called.")
  }
  def renderCode(template: String): String = {
    Template.render(template, Map("class" -> this))
  }

  def getFields = fields.toList.asJava
}
