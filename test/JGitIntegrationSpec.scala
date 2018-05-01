import org.scalatestplus.play.FakeApplicationFactory
import play.api.{Application, ApplicationLoader, Environment}

class JGitIntegrationSpec extends IntegrationSpec with FakeApplicationFactory {
  override def fakeApplication: Application = {
    val context = ApplicationLoader.createContext(Environment.simple())
    new SirdComponents(
      context,
      JGitFileRepositoryPageStoreSpec.buildPageStore()).application
  }
}
