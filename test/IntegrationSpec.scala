import org.openqa.selenium.WebDriver
import org.scalatest.Matchers._
import org.scalatest.selenium.Page
import org.scalatestplus.play._

trait IntegrationSpec
    extends PlaySpec
    with BaseOneServerPerSuite
    with OneBrowserPerTest
    with FakeApplicationFactory
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
    "Create a custom page for /new-path" in {
      go to new Page {
        override val url: String = s"${root}/new-path"
      }
      pageSource should include("not found")
      pageSource should include("submit")

      click on name("content")
      textArea("content").value = "# New stuff\nTest it!"
      submit()
      currentUrl.replaceAllLiterally(root, "") shouldBe "/new-path"
      pageTitle shouldBe "New stuff"
      cssSelector("#content").findElement.value.text shouldBe "New stuff\nTest it!"
    }
    "Edit the customly created page" in {
      go to new Page {
        override val url: String = s"${root}/new-path"
      }
      click on cssSelector("a[rel='edit']")
      pageTitle shouldBe "Editing new-path"
      click on name("content")
      textArea("content").value shouldBe "# New stuff\nTest it!"
      textArea("content").value = "# New stuff\nTest it all!"
      submit()
      currentUrl.replaceAllLiterally(root, "") shouldBe "/new-path"
      pageTitle shouldBe "New stuff"
      cssSelector("#content").findElement.value.text shouldBe "New stuff\nTest it all!"
    }
  }

  implicit override lazy val webDriver: WebDriver =
    HtmlUnitFactory.createWebDriver(false)

}
