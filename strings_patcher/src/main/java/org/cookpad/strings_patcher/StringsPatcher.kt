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

package org.cookpad.strings_patcher

import android.content.Context
import android.content.res.Resources
import android.os.AsyncTask
import android.support.annotation.StringRes
import org.cookpad.strings_patcher.internal.*

private var patches: Map<String, String>? = null

/**
 * Call it as soon as possible and preferably one time per application execution.
 * A good place to call it is Application::onCreate
 *
 * @property context the context of the application
 * @property spreadSheetKey the id of the spreadsheet. For example, given this google docs url:
 * https://docs.google.com/spreadsheets/d/1p65l4BFcIvn6Qaco9nSyUhj-Y6Q-3gpqxtP4wZ24yNM.
 * The id would be 1p65l4BFcIvn6Qaco9nSyUhj-Y6Q-3gpqxtP4wZ24yNM
 * @property worksheetName the worksheet name (The spreadsheet may be composed by several worksheets).
 * This param has as default value the versionCode of the application. That way, your spreadsheet should
 * have as many worksheets as release versions (1,2,3,4,...)
 * @property locale the locale used to filter strings. As default value the system locale is assigned.
 * @property logger callback function to listen for errors emission. As default a dummy implementation does nothing.
 * @property googleCredentials only supply these credentials if the spreadSheet has private access
 */
@JvmOverloads
fun syncStringPatches(context: Context,
                      spreadSheetKey: String,
                      worksheetName: String = versionCode(context),
                      locale: String = defaultLocale,
                      logger: (Throwable) -> Unit = {},
                      googleCredentials: GoogleCredentials? = null) {

    val lastWorksheetName = loadWorksheetName(context)

    if (lastWorksheetName != null && lastWorksheetName != worksheetName) {
        removePatches(context)
    }

    saveWorksheetName(worksheetName, context)

    AsyncTask.execute {
        try {
            patches = loadPatches(context)
            patches = downloadPatches(googleCredentials, spreadSheetKey, worksheetName, locale, context)
            patches?.let { savePatches(it, context) }
        } catch (e: Exception) {
            logger.invoke(e)
        }
    }
}

fun Context.getSmartString(@StringRes stringId: Int): String = resources.getSmartString(stringId)

fun Resources.getSmartString(@StringRes stringId: Int): String =
        patches?.let {
            val key = getResourceName(stringId)?.split("/")?.get(1) ?: ""
            it[key]
        } ?: this.getString(stringId)

