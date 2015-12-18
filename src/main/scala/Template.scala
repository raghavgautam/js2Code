import java.io.StringWriter

import org.apache.velocity.app.VelocityEngine
import org.apache.velocity.tools.ToolManager

object Template {
  val manager = new ToolManager()
  manager.autoConfigure(true)
  private val velocityEngine: VelocityEngine = new VelocityEngine()
  def render(template: String, map: Map[String, Any]): String = {
    val context = manager.createContext()
    map.map(t => context.put(t._1, t._2))
    val w = new StringWriter()
    velocityEngine.evaluate(context, w, "Test template", template)
    w.toString
  }
  def setTemplates(clsTemplate: String, fieldTemplate: String): Unit = {
    javaClsTemplate = clsTemplate
    javaFieldTemplate = fieldTemplate
  }
  private var javaClsTemplate = Util.getResource("class-template.txt")
  def classTemplate = javaClsTemplate
  private var javaFieldTemplate = Util.getResource("field-template.txt")
  def fieldTemplate = javaFieldTemplate
}
object Util {
  def getResource(name: String) = io.Source.fromURL(getClass.getResource(name)).mkString
}