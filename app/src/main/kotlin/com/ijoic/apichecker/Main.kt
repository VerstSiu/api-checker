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

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.ijoic.textio.commonio.toFileText
import com.ijoic.tools.jackson.toEntity

fun main(vararg args: String) {
  val configPath = args.getOrNull(0) ?: throw IllegalArgumentException("config path not found")
  val config = configPath.toFileText(ignoreError = true)?.toEntity(RunConfig::class.java)
    ?: throw IllegalArgumentException("read file content failed: $configPath")

  val manager = CheckManager(HttpApiReader(), fillProtocol = true)

  if (config.apis != null) {
    manager.doApiCheck(config.hosts, config.apis)
  } else if (config.apisAlias != null) {
    manager.doAliasCheck(config.hosts, config.apisAlias)
  }
}

/**
 * Run config
 */
private data class RunConfig @JsonCreator constructor(
  @JsonProperty("hosts") val hosts: List<String>,
  @JsonProperty("apis") val apis: List<String>?,
  @JsonProperty("apis_alias") val apisAlias: List<List<String>>?
)