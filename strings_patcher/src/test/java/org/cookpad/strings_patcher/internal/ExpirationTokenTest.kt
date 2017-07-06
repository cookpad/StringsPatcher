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

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class ExpirationTokenTest {

    @Test fun verifyTokenIsActive() {
        val expirationDate = expireDate(1)
        assertThat(isTokenActive(expirationDate)).isTrue()
    }

    @Test fun verifyTokenIsNotActive() {
        val expirationDate = expireDate(1)
        Thread.sleep(1000)
        assertThat(isTokenActive(expirationDate)).isFalse()
    }
}