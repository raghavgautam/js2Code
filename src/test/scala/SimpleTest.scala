
import java.io.File
import org.apache.commons.io.FileUtils
import org.testng.annotations._
import scala.collection.JavaConverters._

class SimpleTest {

  @DataProvider(name= "jsonProvider")
  def jsonProvider(): Array[Array[Object]] = {
    val simpleJson = this.getClass.getResource("simple.json").getFile
    val resourceDir: File = new File(simpleJson).getParentFile
    val jsonFilter: Array[String] = Array("json")
    val jsonFiles = FileUtils.listFiles(resourceDir, jsonFilter, false)
    val jsonStrings = jsonFiles.asScala.map(io.Source.fromFile(_).mkString).toList
    val ret: List[Array[Object]] = jsonStrings.map(_.asInstanceOf[Object]).map(Array(_))
    ret.toArray
  }

  @Test(dataProvider = "jsonProvider")
  def testJs2Java(jsStr:String) = {
    val javaStr = js2Java.generate(jsStr, "JsRoot")
    println(jsStr)
    javaStr.map(_._2).foreach(println)
  }
}