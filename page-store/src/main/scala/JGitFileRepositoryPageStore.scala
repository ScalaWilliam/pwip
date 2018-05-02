import java.nio.file.{Files, Paths}

import org.eclipse.jgit.api.Git
import org.eclipse.jgit.internal.storage.file.FileRepository
import org.eclipse.jgit.lib.Constants
import org.eclipse.jgit.revwalk.RevWalk

case class JGitFileRepositoryPageStore(repository: FileRepository)
    extends PageStore {

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
      git.add().addFilepattern(s"$name.md").call()
      git.commit().setMessage(s"Update $name").call()
    } finally git.close()
  }

  override def close(): Unit = repository.close()

  override def list(): Set[String] = {
    import org.eclipse.jgit.treewalk.TreeWalk
    val head = repository.resolve(Constants.HEAD)
    if (head == null) Set.empty
    else {
      val walk = new RevWalk(repository)
      try {
        val commit = walk.parseCommit(head)
        val tree = commit.getTree
        val treeWalk = new TreeWalk(repository)
        try {
          treeWalk.reset(tree)
          treeWalk.setRecursive(true)
          var set = Set.empty[String]
          while (treeWalk.next()) {
            if (treeWalk.isSubtree) {
              treeWalk.enterSubtree()
            } else {
              val path = treeWalk.getPathString
              if (path.endsWith(".md")) {
                set = set + path.dropRight(3)
              }
            }
          }
          set
        } finally treeWalk.close()
      } finally walk.close()
    }
  }
}
