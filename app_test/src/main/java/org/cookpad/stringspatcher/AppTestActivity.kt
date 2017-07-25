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

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import org.cookpad.strings_patcher.stringPatcherDebugEnabled

class AppTestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.app_test_activity)

        findViewById(R.id.btRestartActivity).setOnClickListener {
            val intent = intent
            finish()
            startActivity(intent)
        }

        findViewById(R.id.btStringPatcherDebugEnabled).setOnClickListener {
            stringPatcherDebugEnabled = true
        }
    }
}
