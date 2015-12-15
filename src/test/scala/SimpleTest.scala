import org.testng.annotations._

class SimpleTest {

  val simpleJs =
    """
      |{
      |  "id": 7,
      |  "name": "jb"
      |}
    """.stripMargin

  val nestedJs =
    """
      |{
      |  "menu": {
      |    "id": "file",
      |    "value": "File",
      |    "popup": {
      |      "menuitem": [
      |        {"value": "New", "onclick": "CreateNewDoc()"},
      |        {"value": "Open", "onclick": "OpenDoc()"},
      |        {"value": "Close", "onclick": "CloseDoc()"}
      |      ]
      |    }
      |  }
      |}
    """.stripMargin

  @DataProvider(name= "pieceMovesProvider")
  def pieceMovesProvider(): Array[Array[Object]] = {
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