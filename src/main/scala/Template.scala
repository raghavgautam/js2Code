import java.io.StringWriter

import org.apache.velocity.app.VelocityEngine
import org.apache.velocity.tools.ToolManager

object Template {
  val manager = new ToolManager()
  manager.autoConfigure(true)
  private val velocityEngine: VelocityEngine = new VelocityEngine()
  def render(template: String, map: Map[String, Any]): String = {
    if (map == null || map.isEmpty || template == null || template.isEmpty)
      return template
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
  private var javaClsTemplate = Util.getResource("java-class-template.vm")
  def classTemplate = javaClsTemplate
  private var javaFieldTemplate = Util.getResource("java-field-template.vm")
  def fieldTemplate = javaFieldTemplate
}
object Util {
  def getResource(name: String) = io.Source.fromURL(getClass.getResource(name)).mkString
}