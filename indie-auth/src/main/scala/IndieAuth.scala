import play.api.libs.json.{Json, Reads}
import play.api.libs.ws.WSClient
import play.api.mvc.{Action, AnyContent, Call, Results}

import scala.concurrent.{ExecutionContext, Future}
import scala.xml.Elem

case class IndieAuth(rootUrl: Call, callbackUrl: Call)(
    implicit requestHeader: play.api.mvc.RequestHeader) {
  def form: Elem = {
    <form action="https://indieauth.com/auth" method="get">
      <label for="indie_auth_url">Web Address:</label>
      <input id="indie_auth_url" type="text" name="me" placeholder="yourdomain.com"/>
      <p>
        <button type="submit">Sign In</button>
      </p>
      <input type="hidden" name="client_id" value={rootUrl.absoluteURL()}/>
      <input type="hidden" name="redirect_uri" value={callbackUrl.absoluteURL()}/>
    </form>
  }

  def callback(wSClient: WSClient)(
      implicit executionContext: ExecutionContext): Action[AnyContent] =
    Action.async { request =>
      request
        .getQueryString("code")
        .map { code =>
          val postRequest: Map[String, Seq[String]] =
            Map("code" -> Seq(code),
                "redirect_uri" -> Seq(callbackUrl.absoluteURL()),
                "client_id" -> Seq(rootUrl.absoluteURL()))
          wSClient
            .url("https://indieauth.com/auth")
            .post(postRequest)
            .map { response =>
              val user = response.json.as[IndieAuth.HappyResponse]
              Results.Ok(s"${user}")
            }
        }
        .getOrElse(Future.successful(Results.NotFound("Not found")))
    }

}

object IndieAuth {
  case class HappyResponse(me: String) {
    def identifier: String = me
  }
  object HappyResponse {
    implicit val reads: Reads[HappyResponse] = Json.reads[HappyResponse]
  }
}
