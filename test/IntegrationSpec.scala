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
    "not be found for /random-path" in {
      go to new Page {
        override val url: String = s"${root}/random-path"
      }
      pageSource should include("not found")
      pageSource should include("submit")
    }
    "not be creatable for /random-;path (bad URL)" in {
      go to new Page {
        override val url: String = s"${root}/random-;path"
      }
      pageSource should include("Not Found")
      pageSource should not include "submit"
    }
    "Create an intial page" in {
      go to root
      info("we expect the page to not be created initially")
      pageSource should include("not found")
      click on name("content")
      textArea("content").value = "# Index\nTest"
      submit()
      info("once we submit we should see some rendered page")
      info(s"${currentUrl}")
      pageTitle shouldBe "Index"
      cssSelector("#content").findElement.value.text shouldBe "Index\nTest"
    }
  }

  implicit override lazy val webDriver: WebDriver =
    HtmlUnitFactory.createWebDriver(false)

}
