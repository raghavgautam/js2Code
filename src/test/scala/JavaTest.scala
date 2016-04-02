
import java.io.File

import js2code.{Js2Code, Template, Util}
import org.apache.commons.io.FileUtils
import org.testng.Assert
import org.testng.annotations._

import scala.collection.JavaConverters._

class JavaTest {

  @DataProvider(name = "jsonProvider")
  def jsonProvider(): Array[Array[Object]] = {
    def getJsonFiles: Array[File] = {
      val simpleJson = this.getClass.getResource("json/simple.json").getFile
      val resourceDir: File = new File(simpleJson).getParentFile
      val jsonFilter: Array[String] = Array("json")
      FileUtils.listFiles(resourceDir, jsonFilter, false).asScala.toArray
    }
    def asObject(s: String) = s.asInstanceOf[Object]
    val jsonFiles: Array[String] = getJsonFiles.map(_.getName)
    val clsNames = jsonFiles.map(_.split('.').head.capitalize).map(asObject)
    val jsonStrings = jsonFiles.map(Util.getResource)
    jsonStrings.zip(clsNames).map(p => Array(p._1, p._2))
  }

  @Test(dataProvider = "jsonProvider")
  def javaTest(jsStr:String, clsName: String) = {
    Template.setTemplates(Util.getResource("template/java.vm"))
    val javaStr = Js2Code.generate(jsStr, clsName).map(_.toString).reduce(_ + "\n" + _)
    val expected = Util.getResource(s"$clsName.java")
    Assert.assertEquals(javaStr, expected, s"Mismatch for case: $clsName")
  }

  @Test(dataProvider = "jsonProvider")
  def scalaTest(jsStr:String, clsName: String) = {
    Template.setTemplates(Util.getResource("template/scala.vm"))
    val scalaStr = Js2Code.generate(jsStr, clsName).map(_.toString).reduce(_ + "\n" + _)
    val expected = Util.getResource(s"$clsName.scala")
    Assert.assertEquals(scalaStr, expected, s"Mismatch for case: $clsName")
  }
}