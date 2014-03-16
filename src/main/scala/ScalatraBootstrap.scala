import org.scalatra._
import javax.servlet._
import java.util.EnumSet
import org.trebol.rest.MonitorController
import org.trebol.auth.SUAController
import org.trebol.auth.SUAFilter
import org.trebol.rest.DynaObjectController

class ScalatraBootstrap extends LifeCycle {

  override def init(context: ServletContext) {

    //GuiceContainer.startContainer()
    //    context.mount(new SessionsController, "/sessions/*")

    context.mount(new MonitorController, "/monitor/*")
    
    context.mount(new SUAFilter, "/api/*")
    context.mount(new SUAController, "/api/sua/*")

    context.mount(new DynaObjectController, "/api/dyno/*")
  }
}
