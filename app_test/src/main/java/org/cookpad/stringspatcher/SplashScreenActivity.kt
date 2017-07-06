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

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.splash_screen_activity.*
import org.cookpad.strings_patcher.syncStringPatches

class SplashScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_screen_activity)

        val spreadSheetKey = "1p65l4BFcIvn6Qaco9nSyUhj-Y6Q-3gpqxtP4wZ24yNM"
        syncStringPatches(this, spreadSheetKey)

        tvSplashScreen.setOnClickListener {
            startActivity(Intent(this, AppTestActivity::class.java))
        }
    }
}
