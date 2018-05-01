import java.nio.file.{Files, Paths}

import org.eclipse.jgit.api.Git
import org.eclipse.jgit.internal.storage.file.FileRepository

case class JGitFileRepositoryPageStore(repository: FileRepository) extends PageStore {

  override def get(name: String): Option[String] = {
    val path = Paths.get(repository.getDirectory.getParent).resolve(s"$name.md")
    if (Files.exists(path)) Some {
      new String(Files.readAllBytes(path), "UTF-8")
    } else None
  }

  override def contains(name: String): Boolean = {
    Files.exists(
      Paths.get(repository.getDirectory.getParent).resolve(s"$name.md"))
  }

  override def put(name: String, content: String): Unit = {
    val git = new Git(repository)
    try {
      val path =
        Paths.get(repository.getDirectory.getParent).resolve(s"$name.md")
      val bytes = content.getBytes("UTF-8")
      Files.write(path, bytes)
      git.add().addFilepattern(s"*.md").call()
      git.commit().setMessage(s"Update $name").call()
    } finally git.close()
  }

  override def close(): Unit = repository.close()
}

