
import java.io.File
import org.apache.commons.io.FileUtils
import org.testng.annotations._
import scala.collection.JavaConverters._

class SimpleTest {

  @DataProvider(name = "jsonProvider")
  def jsonProvider(): Array[Array[Object]] = {
    def getJsonFiles: Array[File] = {
      val simpleJson = this.getClass.getResource("simple.json").getFile
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
  def testJs2Java(jsStr:String, clsName: String) = {
    val javaStr = js2Java.generate(jsStr, clsName)
    println(jsStr)
    javaStr.foreach(println)
  }
}