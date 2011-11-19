/*
 * Copyright 2011 Heiko Seeberger
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package name.heikoseeberger.sbtsrc

import java.io.File
import sbt.{ Command, Configuration, Keys, Plugin, ProjectRef, State }
import sbt.CommandSupport.logger
import sbt.Configurations.{ Compile, Test }
import scalaz.{ Failure, Success }
import scalaz.Scalaz._

object SrcPlugin extends Plugin {
  override def settings = Seq(Keys.commands += Src.srcCommand)
}

object Src {

  def srcCommand: Command = Command.command("src")(srcAction(_))

  private def srcAction(implicit state: State) = {
    logger(state).info("About to create source directories for you.")
    val srcForProjects = for (ref <- structure.allProjectRefs) yield srcForProject(ref)
    srcForProjects.sequence[ValidationNELString, Seq[(File, Boolean)]] match {
      case Success(projects) =>
        logger(state).info("Successfully created source directories for %s project(s):" format projects.size)
        for {
          project <- projects
          (directory, wasCreated) <- project
        } logger(state).info("%s was %s.".format(directory.getPath, if (wasCreated) "created" else "already there"))
        state
      case Failure(errors) =>
        logger(state).error("Could NOT create source directories for you: " + (errors.list mkString ", "))
        state.fail
    }
  }

  private def srcForProject(ref: ProjectRef)(implicit state: State) =
    (srcDirs(ref, Compile) |@| srcDirs(ref, Test))(createDirectories)

  private def srcDirs(ref: ProjectRef, conf: Configuration)(implicit state: State) =
    setting(Keys.unmanagedSourceDirectories,
      "Missing %s source directories for %s!".format(conf, ref.project),
      ref,
      conf)

  private def createDirectories(compileDirs: Seq[File], testDirs: Seq[File]) =
    for (directory <- (compileDirs ++ testDirs)) yield directory -> directory.mkdirs()
}
