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

package name.heikoseeberger

import sbt.{ Configuration, Configurations, Extracted, Project, ProjectReference, SettingKey, State }
import sbt.complete.Parser
import sbt.Load.BuildStructure
import scalaz.{ NonEmptyList, Validation }
import scalaz.Scalaz._

package object sbtsrc {

  def extracted(implicit state: State): Extracted = Project extract state

  def structure(implicit state: State): BuildStructure = extracted.structure

  def setting[A](
    key: SettingKey[A],
    errorMessage: => String,
    reference: ProjectReference,
    configuration: Configuration = Configurations.Compile)(
      implicit state: State): ValidationNELString[A] = {
    key in (reference, configuration) get structure.data match {
      case Some(a) => a.success
      case None => errorMessage.failNel
    }
  }

  def booleanOpt(key: String): Parser[(String, Boolean)] = {
    import sbt.complete.DefaultParsers._
    (Space ~> key ~ ("=" ~> ("true" | "false"))) map { case (k, v) => k -> v.toBoolean }
  }

  type NELString = NonEmptyList[String]

  type ValidationNELString[A] = Validation[NELString, A]
}
