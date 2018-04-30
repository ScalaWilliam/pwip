import org.openqa.selenium.WebDriver
import org.scalatest.Matchers._
import org.scalatest.selenium.Page
import org.scalatestplus.play._

class IntegrationSpec
    extends PlaySpec
    with BaseOneServerPerSuite
    with TheApplicationFactory
    with OneBrowserPerTest
    with HtmlUnitFactory {

  def root = s"""http://localhost:$port"""

  "Web" must {
    "Not be found for /random-path" in {
      go to new Page {
        override val url: String = s"${root}/random-path"
      }
      pageSource should include("Not Found")
    }
    "Create initial page" in {
      go to root
      info("we expect the page to not be created initially")
      info("we click the set up button which will take us to the edit page")
      click on name("setup")
      info(s"${currentUrl}")
      pageTitle should be("Editing")
      cssSelector("#edit-box").findAllElements mustNot be(empty)
      click on name("body")
      textArea("body").value = "# Index\nTest"
      submit()
      info("once we submit we should see some rendered page")
      pageTitle shouldBe "Index"
      cssSelector("#content").findElement.value.text shouldBe "Index\nTest"
    }
  }

  implicit override lazy val webDriver: WebDriver =
    HtmlUnitFactory.createWebDriver(false)

}
