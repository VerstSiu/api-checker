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

import okhttp3.OkHttpClient
import okhttp3.Request

/**
 * Http api reader
 *
 * @author verstsiu created at 2019-08-19 12:39
 */
internal class HttpApiReader : ApiReader {

  private val client = OkHttpClient()

  override fun getApiContent(url: String): String? {
    val request = Request.Builder()
      .url(url)
      .build()

    val response = client.newCall(request).execute()
    return response.body()?.string()
  }
}