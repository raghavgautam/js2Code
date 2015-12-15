import org.testng.annotations._

import scala.io.Source

class SimpleTest {

  @DataProvider(name= "pieceMovesProvider")
  def pieceMovesProvider(): Array[Array[Object]] = {
  val simpleJs = Source.fromURL(this.getClass.getResource("simple.json")).mkString
  val nestedJs = Source.fromURL(this.getClass.getResource("nested.json")).mkString
    Array(
      Array[Object](simpleJs),
      Array[Object](nestedJs))
  }

  @Test(dataProvider = "pieceMovesProvider")
  def testCanTake(jsStr:String) = {
    val javaStr = Main.getJava(jsStr).head
    print(jsStr)
    println(javaStr)
  }
}