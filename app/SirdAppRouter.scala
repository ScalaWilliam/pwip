import play.api._
import ApplicationLoader.Context
import play.api.mvc._
import play.api.routing._
import play.api.routing.sird._
import Results._
import org.jsoup.Jsoup
import play.twirl.api.Html

class SirdAppLoader extends ApplicationLoader {
  def load(context: Context): Application = {
    new SirdComponents(context).application
  }
}

class SirdComponents(context: Context)
    extends BuiltInComponentsFromContext(context)
    with NoHttpFiltersComponents {

  private var indexPage: Option[String] = Option.empty

  lazy val router: Router = Router.from {
    case GET(p"/") =>
      Action {
        val htmlContent = indexPage match {
          case None => Html.apply("")
          case Some(md) =>
            import com.vladsch.flexmark.html.HtmlRenderer
            import com.vladsch.flexmark.parser.Parser
            import com.vladsch.flexmark.util.options.MutableDataSet
            val options = new MutableDataSet

            options.set(HtmlRenderer.SOFT_BREAK, "<br />\n")

            val parser = Parser.builder(options).build
            val renderer = HtmlRenderer.builder(options).build

            val document = parser.parse(md)
            val html = renderer.render(document)
            Html(html)
        }
        val headingText =
          Option(Jsoup.parse(htmlContent.body).select("h1").first()).map(_.text)
        Ok(views.html.index(headingText.getOrElse("YAY"), htmlContent))
      }
    case POST(p"/setup") =>
      Action {
        Ok(views.html.edit())
      }
    case POST(p"/save") =>
      Action(parse.form(SirdComponents.pushForm)) { request =>
        indexPage = Some(request.body.content)
        SeeOther("/")
      }
    case GET(p"/hello/$to") =>
      Action {
        Ok(s"Hello $to")
      }
  }
}

object SirdComponents {
  import play.api.data._
  import play.api.data.Forms._
  final case class PushContent(content: String)
  val pushForm = Form(
    mapping(
      "content" -> text
    )(PushContent.apply)(PushContent.unapply)
  )
}
