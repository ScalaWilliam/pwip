trait PageStore {
  def get(name: String): Option[String]
  def contains(name: String): Boolean
  def put(name: String, content: String): Unit
  def close(): Unit
}
object PageStore {
  def fromMap(map: Map[String, String]): PageStore = new MapPageStore(map)
  private case class MapPageStore(var map: Map[String, String])
      extends PageStore {
    override def get(name: String): Option[String] = map.get(name)

    override def contains(name: String): Boolean = map.contains(name)

    override def put(name: String, content: String): Unit =
      map = map.updated(name, content)

    override def close(): Unit = ()
  }
}
