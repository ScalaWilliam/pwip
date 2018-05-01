import java.io.File

import org.eclipse.jgit.internal.storage.file.FileRepository
import org.eclipse.jgit.storage.file.FileRepositoryBuilder
import play.api.{Application, ApplicationLoader}
import play.api.ApplicationLoader.Context

class SirdAppLoader extends ApplicationLoader {
  def load(context: Context): Application = {
    val gitRepositoryStoreO =
      context.initialConfiguration
        .getOptional[String]("normal-git-repository")
        .map(barePath => new File(barePath, ".git"))
        .map { bareFile =>
          FileRepositoryBuilder.create(bareFile)
        }
        .collect {
          case fileRepository: FileRepository =>
            JGitFileRepositoryPageStore(fileRepository)
        }
    val inMemoryStore = PageStore.fromMap(Map.empty)
    val pageStore: PageStore = gitRepositoryStoreO.getOrElse(inMemoryStore)
    new SirdComponents(context, pageStore).application
  }
}
