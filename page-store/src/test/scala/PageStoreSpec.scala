import org.scalatest.FreeSpec
import org.scalatest.Matchers._
import org.scalatest.OptionValues._
trait PageStoreSpec {
  this: FreeSpec =>

  def pageStoreBuilder: PageStore

  "it doesn't have stuff by default" in {
    val pageStore = pageStoreBuilder
    pageStore.get("something") shouldBe empty
    assert(!pageStore.contains("something"))
  }
  "it sets something and retrieves it" in {
    val pageStore = pageStoreBuilder
    val key = "A"
    val value = "B"
    pageStore.put(key, value)
    assert(pageStore.contains(key))
    pageStore.put(key, value)
    pageStore.get(key).value shouldBe value
  }
}
