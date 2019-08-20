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

import java.lang.Exception

/**
 * Check manager
 *
 * @author verstsiu created at 2019-08-19 10:45
 */
internal class CheckManager(
  private val apiReader: ApiReader,
  private val isSilent: Boolean = false,
  private val fillProtocol: Boolean = false) {

  /**
   * Returns unmatched result of [hosts] and [apis]
   *
   * Zero means not unmatched record, -1 means matched error
   */
  fun doApiCheck(hosts: List<String>, apis: List<String>): Int {
    if (hosts.isEmpty() || apis.isEmpty()) {
      printMessage("hosts or apis empty")
      return 0
    }
    val hostsFilled = fillHostPrefix(hosts)
    val apiSize = apis.size
    var apiIndex = 0
    var failedSize = 0

    printHostInfo(hosts)

    while (apiIndex < apiSize) {
      val api = apis[apiIndex]
      ++apiIndex

      printTaskInfo("$apiIndex/$apiSize", api)

      try {
        val urls = hostsFilled.map { "$it/$api" }
        val apiResults = urls.map { apiReader.getApiContent(it) }

        if (apiResults.toSet().size != 1) {
          apiResults.forEachIndexed { i, result ->
            printMessage(
              urls[i],
              result.toString(),
              ""
            )
          }
          ++failedSize
        }

      } catch (e: Exception) {
        if (!isSilent) {
          e.printStackTrace()
        }
        return -1
      }
    }

    printMessage("check complete: [$failedSize/$apiSize] failed")
    return failedSize
  }

  /**
   * Returns unmatched result of [hosts] and [alisApis]
   *
   * Zero means not unmatched record, -1 means matched error
   */
  fun doAliasCheck(hosts: List<String>, alisApis: List<List<String>>): Int {
    if (hosts.isEmpty() || alisApis.isEmpty()) {
      printMessage("hosts or apis empty")
      return 0
    }
    val hostsFilled = fillHostPrefix(hosts)
    val apiSize = alisApis.size
    var apiIndex = 0
    var checkSize = 0
    var failedSize = 0

    printHostInfo(hosts)

    while (apiIndex < apiSize) {
      val apis = alisApis[apiIndex]
      ++apiIndex

      printTaskInfo("$apiIndex/$apiSize", apis)

      if (apis.size <= 1) {
        continue
      }
      hostsFilled.forEach { host ->
        ++checkSize

        try {
          val urls = apis.map { "$host/$it" }
          val apiResults = urls.map { apiReader.getApiContent(it) }

          if (apiResults.toSet().size != 1) {
            apiResults.forEachIndexed { i, result ->
              printMessage(
                urls[i],
                result.toString(),
                ""
              )
            }
            ++failedSize
          }

        } catch (e: Exception) {
          if (!isSilent) {
            e.printStackTrace()
          }
          return -1
        }
      }
    }

    printMessage("check complete: [$failedSize/$checkSize] failed")
    return failedSize
  }

  private fun fillHostPrefix(hosts: List<String>): List<String> {
    if (!fillProtocol) {
      return hosts
    }

    return hosts.map { host ->
      when {
        host.startsWith("http://") -> host
        host.startsWith("https://") -> host
        else -> "http://$host"
      }
    }
  }

  private fun printHostInfo(hosts: List<String>) {
    printMessage(
      "hosts: ",
      hosts.joinToString("\n") { "  $it" },
      ""
    )
  }

  private fun printTaskInfo(tag: String, content: Collection<String>) {
    val tagPrefix = "[$tag] "

    content.forEachIndexed { i, text ->
      if (i == 0) {
        printMessage("$tagPrefix$text")
      } else {
        printMessage(text.padStart(tagPrefix.length + text.length))
      }
    }
  }

  private fun printTaskInfo(tag: String, vararg content: String) {
    printTaskInfo(tag, content.toList())
  }

  private fun printMessage(vararg message: String) {
    if (isSilent) {
      return
    }
    message.forEach { println(it) }
  }
}