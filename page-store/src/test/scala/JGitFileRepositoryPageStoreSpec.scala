import java.io.File
import java.nio.file.Files

import org.eclipse.jgit.internal.storage.file.FileRepository
import org.eclipse.jgit.storage.file.FileRepositoryBuilder
import org.scalatest.FreeSpec

class JGitFileRepositoryPageStoreSpec extends FreeSpec with PageStoreSpec {
  override def pageStoreBuilder: PageStore =
    JGitFileRepositoryPageStoreSpec.buildPageStore()
}
object JGitFileRepositoryPageStoreSpec {
  def buildPageStore(): PageStore = {
    val localPath = File.createTempFile("TestGitRepository", "")
    require(localPath.delete())
    Files.createDirectory(localPath.toPath)
    val repository = FileRepositoryBuilder
      .create(new File(localPath, ".git"))
      .asInstanceOf[FileRepository]
    repository.create(false)
    new JGitFileRepositoryPageStore(repository) {
      override def close(): Unit = {
        super.close()
        localPath.delete()
      }
    }
  }
}
