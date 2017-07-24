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

import android.content.Context
import org.json.JSONArray

private val URL_BASE = "https://spreadsheets.google.com/feeds"

internal fun downloadPatches(googleCredentials: GoogleCredentials?, spreadSheetKey: String, worksheetName: String,
                             locale: String, context: Context): Map<String, String> {
    val scope = if (googleCredentials == null) "public" else "private"
    val accessToken = googleCredentials?.let { "&access_token=${accessToken(it, context)}" } ?: ""

    val spreadSheetsInfoUrl = "$URL_BASE/worksheets/$spreadSheetKey/$scope/full?alt=json$accessToken"
    val spreadSheetsInfoEntries = jsonFromGetRequest(spreadSheetsInfoUrl)
            .getJSONObject("feed")
            .getJSONArray("entry")

    val worksheetIndex = getIndexFromWorksheetName(spreadSheetsInfoEntries, worksheetName)

    val spreadSheetContentsUrl = "$URL_BASE/list/$spreadSheetKey/$worksheetIndex/$scope/values?alt=json$accessToken"
    val spreadSheetContentsFeed = jsonFromGetRequest(spreadSheetContentsUrl).getJSONObject("feed")

    if (spreadSheetContentsFeed.has("entry")) {
        val spreadSheetContentsEntries = spreadSheetContentsFeed.getJSONArray("entry")
        val downloadedPatches = getPatchesFromWorksheet(spreadSheetContentsEntries, locale)
        return downloadedPatches
    } else {
        return emptyMap()
    }
}

private fun getIndexFromWorksheetName(entries: JSONArray, worksheetName: String) =
        ((0 until entries.length())
                .indexOfFirst { i -> entries.getNestedString(i, "title") == worksheetName }
                .takeIf { it != -1 } ?: throw RuntimeException("Spreadsheet not found")) + 1

private fun getPatchesFromWorksheet(entries: JSONArray, locale: String) =
        (0 until entries.length())
                .filter { i -> entries.getNestedString(i, "gsx\$lang") == locale }
                .map { i ->
                    val key = entries.getNestedString(i, "gsx\$key")
                    val value = entries.getNestedString(i, "gsx\$value")
                    key to value
                }
                .toMap()

private fun JSONArray.getNestedString(index: Int, key: String) =
        this.getJSONObject(index)
                .getJSONObject(key)
                .getString("\$t")
