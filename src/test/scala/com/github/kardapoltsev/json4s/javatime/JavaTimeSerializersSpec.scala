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

import java.time.temporal.TemporalAccessor
import java.time._

import org.json4s.{DefaultFormats, MappingException}
import org.json4s.native.Serialization
import org.scalatest.{Matchers, WordSpec}

class JavaTimeSerializersSpec extends WordSpec with Matchers {
  implicit val formats = DefaultFormats ++ JavaTimeSerializers.defaults

  private val sample = ZonedDateTime.of(
    2017, 1, 31,
    19, 1, 50, 3,
    ZoneId.of("Asia/Yekaterinburg")
  )

  "JavaTimeSerializers" should {
    "serialize/deserialize Instant" in {
      val in = Instant.from(sample)
      test(in, "2017-01-31T14:01:50.000000003Z")
    }
    "serialize/deserialize LocalDateTime" in {
      val in = LocalDateTime.from(sample)
      test(in, "2017-01-31T19:01:50.000000003")
    }
    "serialize/deserialize LocalTime" in {
      val in = LocalTime.from(sample)
      test(in, "19:01:50.000000003")
    }
    "serialize/deserialize OffsetDateTime" in {
      val in = OffsetDateTime.from(sample)
      test(in, "2017-01-31T19:01:50.000000003+05:00")
    }
    "serialize/deserialize OffsetTime" in {
      val in = OffsetTime.from(sample)
      test(in, "19:01:50.000000003+05:00")
    }
    "serialize/deserialize ZonedDateTime" in {
      val in = sample
      test(in, "2017-01-31T19:01:50.000000003+05:00[Asia/Yekaterinburg]")
    }
  }

  private def test[A <: AnyRef with TemporalAccessor: Manifest](in: A, expected: String): Unit = {
    val wrapped    = Wrapper(in)
    val serialized = Serialization.write(wrapped)
    println(serialized)
    serialized shouldBe s"""{"value":"$expected"}"""
    val deserialized = Serialization.read[Wrapper[A]](serialized)
    deserialized shouldBe wrapped

    val broken = s"""{"value":"${expected.drop(1)}"}"""
    a[MappingException] shouldBe thrownBy {
      Serialization.read[Wrapper[A]](broken)
    }
  }
}

case class Wrapper[A <: TemporalAccessor](value: A)
