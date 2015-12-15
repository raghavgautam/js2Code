
import java.io.File
import org.apache.commons.io.FileUtils
import org.testng.annotations._
import scala.collection.JavaConverters._

class SimpleTest {

  @DataProvider(name= "jsonProvider")
  def jsonProvider(): Array[Array[Object]] = {
    def getJsonFiles: Array[File] = {
      val simpleJson = this.getClass.getResource("simple.json").getFile
      val resourceDir: File = new File(simpleJson).getParentFile
      val jsonFilter: Array[String] = Array("json")
      val jsonFiles = FileUtils.listFiles(resourceDir, jsonFilter, false).asScala.toList
      jsonFiles.toArray
    }
    def asObject(s: String) = s.asInstanceOf[Object]
    val jsonFiles: Array[File] = getJsonFiles
    val jsonFileNames = jsonFiles.map(_.getName.split('.').head.capitalize).map(asObject)
    val jsonStrings = jsonFiles.map(io.Source.fromFile(_).mkString).map(asObject)
    jsonStrings.zip(jsonFileNames).map(p => Array(p._1, p._2))
  }

  @Test(dataProvider = "jsonProvider")
  def testJs2Java(jsStr:String, clsName: String) = {
    val javaStr = js2Java.generate(jsStr, clsName)
    println(jsStr)
    javaStr.map(_._2).foreach(println)
  }
}