import org.scalatest.FreeSpec

class MapPageStoreSpec extends FreeSpec with PageStoreSpec {
  override def pageStoreBuilder: PageStore = PageStore.fromMap(Map.empty)
}
