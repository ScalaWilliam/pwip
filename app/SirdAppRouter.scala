import play.api._
import ApplicationLoader.Context
import play.api.mvc._
import play.api.routing._
import play.api.routing.sird._
import Results._

class SirdAppLoader extends ApplicationLoader {
  def load(context: Context): Application = {
    new SirdComponents(context).application
  }
}

class SirdComponents(context: Context)
    extends BuiltInComponentsFromContext(context)
    with NoHttpFiltersComponents {
  lazy val router: Router = Router.from {
    case GET(p"/") =>
      Action {
        Ok(views.html.index())
      }
    case GET(p"/hello/$to") =>
      Action {
        Ok(s"Hello $to")
      }
  }
}
