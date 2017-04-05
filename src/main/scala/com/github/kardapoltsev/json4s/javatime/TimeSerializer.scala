/*
  Copyright 2017 Alexey Kardapoltsev

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
 */
package com.github.kardapoltsev.json4s.javatime

import java.time.format.{DateTimeFormatter, DateTimeParseException}
import java.time.temporal.{TemporalAccessor, TemporalQuery}

import org.json4s.{Formats, JValue, MappingException, Serializer, TypeInfo}
import org.json4s.JsonAST.JString

abstract class TimeSerializer[A <: TemporalAccessor](implicit manifest: Manifest[A])
    extends Serializer[A] {

  private val Class = manifest.runtimeClass
  protected def format: DateTimeFormatter
  protected def temporalQuery: TemporalQuery[A]

  def deserialize(implicit ft: Formats): PartialFunction[(TypeInfo, JValue), A] = {
    case (TypeInfo(Class, _), JString(s)) =>
      try {
        format.parse(s, temporalQuery)
      } catch {
        case e: DateTimeParseException =>
          throw MappingException("Can't convert `" + s + "' to " + Class, e)
      }
  }

  def serialize(implicit fmt: Formats): PartialFunction[Any, JValue] = {
    case v: A => JString(format.format(v))
  }

  protected def asQuery(f: TemporalAccessor => A): TemporalQuery[A] = {
    new TemporalQuery[A] {

      override def queryFrom(temporal: TemporalAccessor): A = f(temporal)
    }
  }

}
