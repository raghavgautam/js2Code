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

package demo

import js2code.{Js2Code, Template, Util}

object Demo extends App {
  Template.setTemplates(Util.getResource("template/demo.vm"))
  val jsStr = io.Source.fromURL("https://api.github.com/users/raghavgautam").mkString
  val scalaCode = Js2Code.generate(jsStr, "GithubUser").map(_.toString).reduce(_ + "\n" + _)
  println("*"*10 + " SCALA CODE " + "*"*10)
  println(scalaCode)
  println("*"*10 + " PYTHON CODE " + "*"*10)
  Template.setTemplates(Util.getResource("template/python.vm"))
  val pythonCode = Js2Code.generate(jsStr, "GithubUser").map(_.toString).reduce(_ + "\n" + _)
  print(pythonCode)
}
