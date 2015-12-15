
import org.testng.annotations._

import scala.collection.JavaConverters._

class SimpleTest {

  @DataProvider(name= "pieceMovesProvider")
  def pieceMovesProvider(): Array[Array[Object]] = {
    val resources = this.getClass.getClassLoader.getResources("simple.json").asScala.toList
    val jsons: List[Object] = resources.map(io.Source.fromURL(_).mkString).map(_.asInstanceOf[Object])
    val ret: List[Array[Object]] = jsons.map(Array(_))
    ret.toArray
  }

  @Test(dataProvider = "pieceMovesProvider")
  def testCanTake(jsStr:String) = {
    val javaStr = Main.getJava(jsStr).head
    print(jsStr)
    println(javaStr)
  }
}