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

import java.io.PrintWriter

import js2code.{Js2Code, Template, Util}

import scala.sys.process.Process
import scala.util.control.Exception

object PythonDemo extends App {
  val pythonTemplate = Util.getResource("/template/python.vm")
  val pathInfo = Array(
    ("/Users/rgautam/tmp/topo-summary.json", "TopologiesSummary",
      "/Users/rgautam/certification/HDPTests/beaver/component/dataStructure/storm_rest/topology/topology_summary.py"),
    ("/Users/rgautam/tmp/topology.json", "TopologySummary",
      "/Users/rgautam/certification/HDPTests/beaver/component/dataStructure/storm_rest/topology/topology_info.py"),
    ("/Users/rgautam/tmp/spout-word.json", "Spout",
      "/Users/rgautam/certification/HDPTests/beaver/component/dataStructure/storm_rest/topology/spout_info.py"),
    ("/Users/rgautam/tmp/bolt-exclaim1.json", "Bolt",
      "/Users/rgautam/certification/HDPTests/beaver/component/dataStructure/storm_rest/topology/bolt_info.py"),
    ("/Users/rgautam/tmp/ambari-clusters.json", "AmbariClusters",
      "/Users/rgautam/certification/HDPTests/beaver/component/dataStructure/storm_rest/ambari/ambari_clusters.py"),
    ("/Users/rgautam/tmp/ambari-storm-ui-summary.json", "AmbariStormUiSummary",
      "/Users/rgautam/certification/HDPTests/beaver/component/dataStructure/storm_rest/ambari/ambari_storm_ui_summary.py"))

  pathInfo.foreach { onePinfo =>
    val jsStr = io.Source.fromFile(onePinfo._1).mkString
    val initClsName: String = onePinfo._2
    val scalaStr = Js2Code.generate(jsStr, initClsName).map(_.renderCode(pythonTemplate)).reduce(_ + "\n" + _)
    val pyFile = onePinfo._3
    new PrintWriter(pyFile) {
      write(scalaStr)
      close()
    }
    print(scalaStr)
    val lint = Process("pylint-2.7", Seq(pyFile))
    lint.run()
    Exception.catching(classOf[RuntimeException]).withApply(e => println(s"pylint problems: ${e.getMessage}")) {
      lint.lineStream.reduce(_ + _).foreach(println)
    }
    println(s"Can use: $pyFile")
    println(scalaStr.length)
  }
}
