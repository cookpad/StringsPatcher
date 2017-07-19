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
import java.io.File
import java.io.FileInputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

private val PREFS_NAME = "org.cookpad.strings_patcher"
private val KEY_EXPIRATION_DATE = "key_expiration_date"
private val KEY_ACCESS_TOKEN = "key_access_token"
private val KEY_WORKSHEET_NAME = "key_worksheet_name"

internal fun savePatches(patches: Map<String, String>, context: Context) {
    val fos = context.openFileOutput("StringPatches.ser", Context.MODE_PRIVATE)
    ObjectOutputStream(fos).use { it.writeObject(patches) }
}

internal fun loadPatches(context: Context): Map<String, String>? {
    val file = File(pathPatches(context))
    if (!file.exists()) return null

    val fileInputStream = FileInputStream(file)
    ObjectInputStream(fileInputStream).use { return it.readObject() as Map<String, String> }
}

internal fun removePatches(context: Context) {
    val file = File(pathPatches(context))
    if (file.exists()) file.delete()
}

private fun pathPatches(context: Context): String = "${context.filesDir}/StringPatches.ser"

internal fun saveTokenAndExpirationDate(accessToken: String, expirationDate: Long, context: Context) =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                .edit()
                .putLong(KEY_EXPIRATION_DATE, expirationDate)
                .putString(KEY_ACCESS_TOKEN, accessToken)
                .apply()

internal fun loadTokenAndExpirationDate(context: Context): Pair<String?, Long> {
    val settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    val accessToken = settings.getString(KEY_ACCESS_TOKEN, null)
    val expirationDate = settings.getLong(KEY_EXPIRATION_DATE, 0)
    return accessToken to expirationDate
}

internal fun saveWorksheetName(worksheetName: String, context: Context) {
    context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit()
            .putString(KEY_WORKSHEET_NAME, worksheetName)
            .apply()
}

internal fun loadWorksheetName(context: Context): String? =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                .getString(KEY_WORKSHEET_NAME, null)

