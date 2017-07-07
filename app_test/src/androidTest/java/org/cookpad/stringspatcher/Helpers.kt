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

package org.cookpad.stringspatcher

import android.content.Context
import android.support.test.InstrumentationRegistry
import org.assertj.core.api.Assertions.assertThat
import org.cookpad.strings_patcher.getSmartString
import org.cookpad.strings_patcher.internal.GoogleCredentials
import org.cookpad.strings_patcher.syncStringPatches
import java.io.File

private fun sleep() = Thread.sleep(4000)

fun verifyFailureSuccessWorksheet(googleCredentials: GoogleCredentials? = null) {
    removeToken()

    sleep()
    //Remove patches
    val context = InstrumentationRegistry.getTargetContext()
    val file = File("${context.filesDir}/StringPatches.ser")
    if (file.exists()) file.delete()

    var errorMessage = ""
    syncStringPatches(context, "Boom!!", googleCredentials = googleCredentials, logger = { errorMessage = it.toString() })

    sleep()

    val welcomeMessage = context.getSmartString(R.string.welcome_message)
    assertThat(welcomeMessage).isEqualTo("Welcome message")
    assertThat(errorMessage).contains("java.io.FileNotFoundException: https://spreadsheets.google.com/feeds/worksheets/Boom!!/")
}

fun verifySuccessWorksheet(worksheetName: String, googleCredentials: GoogleCredentials? = null) {
    val context = InstrumentationRegistry.getTargetContext()

    syncStringPatches(context, worksheetName, googleCredentials = googleCredentials)

    sleep()

    val welcomeMessage = context.getSmartString(R.string.welcome_message)
    assertThat(welcomeMessage).isEqualTo("Hi Updated!")
}

fun verifyFailureAfterSuccessWorksheet(googleCredentials: GoogleCredentials? = null) {
    val context = InstrumentationRegistry.getTargetContext()
    syncStringPatches(context, "Boom!!", googleCredentials = googleCredentials)

    sleep()

    val welcomeMessage = context.getSmartString(R.string.welcome_message)
    assertThat(welcomeMessage).isEqualTo("Hi Updated!")
}

fun verifyAfterChangeWorksheetNamePreviousIsDeleted(googleCredentials: GoogleCredentials? = null) {
    val context = InstrumentationRegistry.getTargetContext()
    syncStringPatches(context, "Boom!!", worksheetName = "whatever", googleCredentials = googleCredentials)

    sleep()

    val welcomeMessage = context.getSmartString(R.string.welcome_message)
    assertThat(welcomeMessage).isEqualTo("Welcome message")
}

fun verifySuccessAnotherValidWorksheetNameWorksheet(worksheetName: String, googleCredentials: GoogleCredentials? = null) {
    val context = InstrumentationRegistry.getTargetContext()
    syncStringPatches(context, worksheetName, worksheetName = "2", googleCredentials = googleCredentials)

    sleep()

    val welcomeMessage = context.getSmartString(R.string.welcome_message)
    assertThat(welcomeMessage).isEqualTo("Hi 2 Updated!")
}

fun verifySuccessAnotherLocaleWorksheet(worksheetName: String, googleCredentials: GoogleCredentials? = null) {
    val context = InstrumentationRegistry.getTargetContext()
    syncStringPatches(context, worksheetName, locale = "es", googleCredentials = googleCredentials)

    sleep()

    val welcomeMessage = context.getSmartString(R.string.welcome_message)
    assertThat(welcomeMessage).isEqualTo("Hola Actualizado!")
}


fun verifyFailureDueToWrongCredentials(worksheetName: String) {
    removeToken()

    val context = InstrumentationRegistry.getTargetContext()
    var errorMessage = ""

    syncStringPatches(context, worksheetName, locale = "es",
            googleCredentials = GoogleCredentials("imgod", "openthatdoor", "now"), logger = { errorMessage = it.toString() })

    sleep()

    assertThat(errorMessage).contains("The OAuth client was not found.")
}

private fun removeToken() =
        InstrumentationRegistry.getTargetContext()
                .getSharedPreferences("org.cookpad.strings_patcher", Context.MODE_PRIVATE)
                .edit()
                .putString("key_access_token", null)
                .apply()
