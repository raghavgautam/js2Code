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
  val classTemplate: String = io.Source.fromURL(getClass.getResource("class-template.txt")).mkString
  val fieldTemplate: String = io.Source.fromURL(getClass.getResource("field-template.txt")).mkString
}
