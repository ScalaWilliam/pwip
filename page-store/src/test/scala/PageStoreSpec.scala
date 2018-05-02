import org.scalatest.FreeSpec
import org.scalatest.Matchers._
import org.scalatest.OptionValues._
trait PageStoreSpec {
  this: FreeSpec =>

  def pageStoreBuilder: PageStore

  def withPageStore[T](f: PageStore => T): T = {
    val pageStore = pageStoreBuilder
    try f(pageStore)
    finally pageStore.close()
  }

  "it doesn't have stuff by default" in withPageStore { pageStore =>
    assert(!pageStore.contains("something"))
    pageStore.get("something") shouldBe empty
    pageStore.list() shouldBe empty
  }
  "it sets something and retrieves it" in withPageStore { pageStore =>
    val key = "A"
    val value = "B"
    pageStore.put(key, value)
    assert(pageStore.contains(key))
    pageStore.put(key, value)
    pageStore.get(key).value shouldBe value
    pageStore.list() should contain only (key)
  }
  "it sets multiple things and retrieves a list" in withPageStore { pageStore =>
    val key = "A"
    val key2 = "C"
    val value = "B"
    pageStore.put(key, value)
    pageStore.put(key2, value)
    pageStore.list() should contain only (key, key2)
  }
}
