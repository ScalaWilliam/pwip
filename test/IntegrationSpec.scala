import org.openqa.selenium.WebDriver
import org.scalatest.Matchers._
import org.scalatestplus.play._

class IntegrationSpec
    extends PlaySpec
    with BaseOneServerPerSuite
    with TheApplicationFactory
    with OneBrowserPerTest
    with HtmlUnitFactory {

  "Web" must {
    "Create initial page" in {
      go to root
      info("we expect the page to not be created initially")
      info("we click the set up button which will take us to the edit page")
      click on id("setup")
      eventually {
        pageTitle should be("Editing")
        cssSelector("#edit-box").findAllElements mustNot be(empty)
        click on name("body")
        textArea("body").value = "# Index\nTest"
        submit()
        info("once we submit we should see some rendered page")
        eventually {
          pageTitle shouldBe "Index"
          cssSelector("#content").findElement.value.text shouldBe "Index\nTest"
        }
      }
    }
  }

  implicit override lazy val webDriver: WebDriver =
    HtmlUnitFactory.createWebDriver(false)

  def root = s"""http://localhost:$port"""

}
