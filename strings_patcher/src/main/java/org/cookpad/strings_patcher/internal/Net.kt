/*
 * Copyright 2017 Cookpad Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.cookpad.strings_patcher.internal

import android.net.Uri
import org.json.JSONObject
import java.io.BufferedInputStream
import java.io.BufferedWriter
import java.io.IOException
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL


internal fun jsonFromGetRequest(url: String): JSONObject {
    var urlConnection: HttpURLConnection? = null
    return try {
        urlConnection = URL(url).openConnection() as HttpURLConnection
        val input = BufferedInputStream(urlConnection.inputStream)
        val jsonString = input.bufferedReader().use { it.readText() }
        JSONObject(jsonString)
    } finally {
        urlConnection?.disconnect()
    }
}

internal fun jsonFromPostRequest(url: String, params: Uri.Builder): JSONObject {
    var urlConnection: HttpURLConnection? = null
    return try {
        urlConnection = (URL(url).openConnection() as HttpURLConnection).apply {
            readTimeout = 10000
            connectTimeout = 15000
            requestMethod = "POST"
            doInput = true
            doOutput = true
        }

        val query = params.build().encodedQuery

        BufferedWriter(OutputStreamWriter(urlConnection.outputStream, "UTF-8")).use {
            it.write(query)
            it.flush()
        }

        urlConnection.connect()

        if (urlConnection.responseCode != 200) {
            val inputError = BufferedInputStream(urlConnection.errorStream)
            val string = inputError.bufferedReader().use { it.readText() }
            throw RuntimeException(string)
        }

        val input = BufferedInputStream(urlConnection.inputStream)
        val jsonString = input.bufferedReader().use { it.readText() }
        JSONObject(jsonString)
    } finally {
        urlConnection?.disconnect()
    }
}