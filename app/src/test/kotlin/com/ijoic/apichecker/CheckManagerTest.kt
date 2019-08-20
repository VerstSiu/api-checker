/*
 *
 *  Copyright(c) 2019 VerstSiu
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */
package com.ijoic.apichecker

import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.doThrow
import com.nhaarman.mockito_kotlin.mock
import org.junit.Test

/**
 * Check manager test
 *
 * @author verstsiu created at 2019-08-19 10:51
 */
internal class CheckManagerTest {
  @Test
  fun testSimple() {
    val hosts = listOf("h1", "h2")
    val apis = listOf("a1", "a2")

    val apiReader: ApiReader = mock {
      on { getApiContent("h1/a1") } doReturn "xxx"
      on { getApiContent("h2/a1") } doReturn "xxs"
      on { getApiContent("h1/a2") } doReturn "XXX"
      on { getApiContent("h2/a2") } doThrow IllegalArgumentException("path not found")
    }

    val manager = CheckManager(apiReader)
    val resultCode = manager.doApiCheck(hosts, apis)
    assert(resultCode == -1)
  }

  @Test
  fun testAlias() {
    val hosts = listOf("h1")
    val apiAlias = listOf(
      listOf("a1/i1", "a1/i2"),
      listOf("a2/i1", "a2/i2")
    )

    val apiReader: ApiReader = mock {
      on { getApiContent("h1/a1/i1") } doReturn "xxx"
      on { getApiContent("h1/a1/i2") } doReturn "xxx"
      on { getApiContent("h1/a2/i1") } doReturn "XXX"
      on { getApiContent("h1/a2/i2") } doReturn "ZZZ"
    }

    val manager = CheckManager(apiReader)
    val resultCode = manager.doAliasCheck(hosts, apiAlias)
    assert(resultCode == 1)
  }
}