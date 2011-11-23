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
import sbt.{ Command, Configuration, Keys, Plugin, ProjectRef, Setting, SettingKey, State }
import sbt.CommandSupport.logger
import sbt.Configurations.{ Compile, Test }
import scalaz.{ Failure, Success }
import scalaz.Scalaz._

object SrcPlugin extends Plugin {

  override def settings = {
    import SrcKeys._
    Seq(
      withManaged := false,
      withResources := true,
      Keys.commands <+= (withManaged, withResources)(srcCommand))
  }

  private def srcCommand(withManaged: Boolean, withResources: Boolean): Command = {
    def act(state: State, args: Seq[(String, Boolean)]) =
      action(withManaged, withResources, args.toMap)(state)
    Command("src")(_ => parser)(act)
  }

  private def parser = {
    import SrcOpts._
    val opt = booleanOpt(WithManaged) | booleanOpt(WithResources)
    opt.*
  }

  private def action(
    withManaged: Boolean,
    withResources: Boolean,
    args: Map[String, Boolean])(
      implicit state: State) = {
    logger(state).info("About to create source directories for your project(s).")
    val srcForProjects = {
      import SrcOpts._
      for (ref <- structure.allProjectRefs) yield srcForProject(
        ref,
        args get WithManaged getOrElse withManaged,
        args get WithResources getOrElse withResources)
    }
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

  private def srcForProject(ref: ProjectRef, withManaged: Boolean, withResources: Boolean)(implicit state: State) = {
    val compileDirs = dirs(ref, Compile, withManaged, withResources)
    val testDirs = dirs(ref, Test, withManaged, withResources)
    (compileDirs |@| testDirs)(createDirs)
  }

  private def dirs(
    ref: ProjectRef,
    conf: Configuration,
    withManaged: Boolean,
    withResources: Boolean)(
      implicit state: State) =
    Seq(
      setting(
        if (withManaged) Keys.sourceDirectories else Keys.unmanagedSourceDirectories,
        "Missing %s source directories for %s!".format(conf, ref.project),
        ref,
        conf),
      if (!withResources) Seq.empty.success else setting(
        if (withManaged) Keys.resourceDirectories else Keys.unmanagedResourceDirectories,
        "Missing %s resource directories for %s!".format(conf, ref.project),
        ref,
        conf)).sequence map { _.flatten }

  private def createDirs(compileDirs: Seq[File], testDirs: Seq[File]) =
    for (directory <- (compileDirs ++ testDirs)) yield directory -> directory.mkdirs()

  object SrcKeys {
    import SrcOpts._

    val withManaged: SettingKey[Boolean] =
      SettingKey[Boolean](prefix(WithManaged), "Create managed source directories?")

    val withResources: SettingKey[Boolean] =
      SettingKey[Boolean](prefix(WithResources), "Create resource directories?")

    private def prefix(key: String) = "src-" + key
  }

  private object SrcOpts {

    val WithManaged = "with-managed"

    val WithResources = "with-resources"
  }
}
