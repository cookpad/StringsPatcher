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

import android.support.test.runner.AndroidJUnit4
import org.cookpad.strings_patcher.internal.GoogleCredentials
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters

@RunWith(AndroidJUnit4::class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class StringPatcherPrivateTest {
    private val googleCredentials = GoogleCredentials(refreshToken, clientId, clientSecret)

    @Test fun step1VerifyFailureSuccessPrivateWorksheet() = verifyFailureSuccessWorksheet(googleCredentials)

    @Test fun step2VerifySuccessPrivateWorksheet() = verifySuccessWorksheet(privateWorksheetName, googleCredentials)

    @Test fun step3VerifyFailureAfterSuccessPrivateWorksheet() = verifyFailureAfterSuccessWorksheet(googleCredentials)

    @Test fun step4VerifyAfterPrivateChangeWorksheetNamePreviousIsDeleted() = verifyAfterChangeWorksheetNamePreviousIsDeleted(googleCredentials)

    @Test fun step5VerifySuccessAnotherValidWorksheetNamePrivateWorksheet() = verifySuccessAnotherValidWorksheetNameWorksheet(privateWorksheetName, googleCredentials)

    @Test fun step6VerifySuccessAnotherLocalePrivateWorksheet() = verifySuccessAnotherLocaleWorksheet(privateWorksheetName, googleCredentials)
}
