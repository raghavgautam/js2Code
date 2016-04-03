
import java.io.File

import js2code.{Js2Code, Util}
import org.apache.commons.io.FileUtils
import org.testng.Assert
import org.testng.annotations._

import scala.collection.JavaConverters._

class JavaTest {

  @DataProvider(name = "jsonProvider")
  def jsonProvider(): Array[Array[Object]] = {
    def getJsonFiles: Array[File] = {
      val jsonDir = this.getClass.getResource("json").getFile
      val resourceDir: File = new File(jsonDir).getParentFile
      val jsonFilter: Array[String] = Array("json")
      FileUtils.listFiles(new File(jsonDir), jsonFilter, false).asScala.toArray
    }
    def asObject(s: String) = s.asInstanceOf[Object]
    val jsonFiles: Array[String] = getJsonFiles.map(_.getName)
    val clsNames = jsonFiles.map(_.split('.').head.capitalize).map(asObject)
    val jsonStrings = jsonFiles.map(j => Util.getResource("/json/" + j))
    jsonStrings.zip(clsNames).map(p => Array(p._1, p._2))
  }

  @Test(dataProvider = "jsonProvider")
  def javaTest(jsStr:String, clsName: String) = {
    val javaTemplate = Util.getResource("/template/java.vm")
    val javaStr = Js2Code.generate(jsStr, clsName).map(_.renderCode(javaTemplate)).reduce(_ + "\n" + _)
    val expected = Util.getResource(s"/output/java/$clsName.java")
    Assert.assertEquals(javaStr, expected, s"Mismatch for case: $clsName")
  }

  @Test(dataProvider = "jsonProvider")
  def scalaTest(jsStr:String, clsName: String) = {
    val scalaTemplate = Util.getResource("/template/scala.vm")
    val scalaStr = Js2Code.generate(jsStr, clsName).map(_.renderCode(scalaTemplate)).reduce(_ + "\n" + _)
    val expected = Util.getResource(s"/output/scala/$clsName.scala")
    Assert.assertEquals(scalaStr, expected, s"Mismatch for case: $clsName")
  }
}