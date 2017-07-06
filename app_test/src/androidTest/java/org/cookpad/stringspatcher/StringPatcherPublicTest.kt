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
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters


@RunWith(AndroidJUnit4::class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class StringPatcherPublicTest {
    @Test fun step1VerifyFailureSuccessPublicWorksheet() = verifyFailureSuccessWorksheet()

    @Test fun step2VerifySuccessPublicWorksheet() = verifySuccessWorksheet(publicWorksheetName)

    @Test fun step3VerifyFailureAfterSuccessPublicWorksheet() = verifyFailureAfterSuccessWorksheet()

    @Test fun step4VerifyAfterPublicChangeWorksheetNamePreviousIsDeleted() = verifyAfterChangeWorksheetNamePreviousIsDeleted()

    @Test fun step5VerifySuccessAnotherValidWorksheetNamePublicWorksheet() = verifySuccessAnotherValidWorksheetNameWorksheet(publicWorksheetName)

    @Test fun step6VerifySuccessAnotherLocalePublicWorksheet() = verifySuccessAnotherLocaleWorksheet(publicWorksheetName)
}
