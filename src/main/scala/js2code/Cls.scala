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

/**
  * Created by rgautam on 4/1/16.
  */
case class Cls(name: WrappedString, fields: Iterable[Field]) {
  override def toString: String = {
    Template.render(Template.classTemplate, Map("class" -> this))
  }

  def getFields: List[Field] = fields.toList
}
