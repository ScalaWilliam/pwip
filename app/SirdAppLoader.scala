import play.api.{Application, ApplicationLoader}
import play.api.ApplicationLoader.Context

class SirdAppLoader extends ApplicationLoader {
  def load(context: Context): Application = {
    new SirdComponents(context, PageStore.fromMap(Map.empty)).application
  }
}
