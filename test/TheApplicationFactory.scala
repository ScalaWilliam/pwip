import org.scalatestplus.play.FakeApplicationFactory
import play.api.{Application, ApplicationLoader, Environment}

trait TheApplicationFactory extends FakeApplicationFactory {
  override def fakeApplication: Application = {
    val context = ApplicationLoader.createContext(Environment.simple())
    val loader = new SirdAppLoader()
    loader.load(context)
  }
}
