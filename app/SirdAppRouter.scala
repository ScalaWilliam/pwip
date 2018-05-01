import play.api._
import ApplicationLoader.Context
import play.api.mvc._
import play.api.routing._
import play.api.routing.sird._
import Results._
import org.jsoup.Jsoup
import play.twirl.api.Html

import scala.util.matching.Regex

class SirdAppLoader extends ApplicationLoader {
  def load(context: Context): Application = {
    new SirdComponents(context).application
  }
}

class SirdComponents(context: Context)
    extends BuiltInComponentsFromContext(context)
    with NoHttpFiltersComponents {

  private var pages: Map[String, String] = Map.empty

  def renderPage(id: String, markdown: String): Result = {
    val html = {
      import com.vladsch.flexmark.html.HtmlRenderer
      import com.vladsch.flexmark.parser.Parser
      import com.vladsch.flexmark.util.options.MutableDataSet
      val options = new MutableDataSet

      options.set(HtmlRenderer.SOFT_BREAK, "<br />\n")

      val parser = Parser.builder(options).build
      val renderer = HtmlRenderer.builder(options).build

      val document = parser.parse(markdown)
      val html = renderer.render(document)
      Html(html)
    }
    val headingText =
      Option(Jsoup.parse(html.body).select("h1").first()).map(_.text)
    Ok(views.html.view_page(id, headingText.getOrElse("YAY"), html))
  }

  lazy val router: Router = Router.from {
    case GET(p"/") if !pages.contains("index") =>
      Action {
        Ok(views.html.create_not_found(pageId = "index"))
      }
    case GET(p"/") if pages.contains("index") =>
      Action {
        renderPage("index", pages("index"))
      }
    case POST(p"/create-page" ? q"page-id=$page") =>
      Action(parse.form(SirdComponents.pushForm)) { request =>
        pages = pages + (page -> request.body.content)
        val targetUrl = if (page == "index") "/" else s"/$page"
        SeeOther(targetUrl)
      }
    case GET(p"/$path*")
        if SirdComponents.validPath.findFirstIn(path).isDefined =>
      Action {
        NotFound(views.html.create_not_found(pageId = path))
      }
  }
}

object SirdComponents {
  val validPath: Regex = "^(?U)\\p{IsAlphabetic}[\\p{Alnum}_-]+$".r
  import play.api.data._
  import play.api.data.Forms._
  final case class PushContent(content: String)
  val pushForm = Form(
    mapping(
      "content" -> text
    )(PushContent.apply)(PushContent.unapply)
  )
}
