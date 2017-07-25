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
import android.content.res.Resources
import android.os.Build
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import org.cookpad.strings_patcher.addDebug
import org.cookpad.strings_patcher.keysValuesResources
import org.cookpad.strings_patcher.patches
import java.lang.reflect.Modifier

internal fun versionCode(context: Context): String =
        context.packageManager.getPackageInfo(context.packageName, 0).versionCode.toString()

internal fun defaultLocale(): String =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Resources.getSystem().configuration.locales[0].language
        } else {
            Resources.getSystem().configuration.locale.language
        }

internal fun getAllKeysValuesResources(clazz: Class<*>, context: Context): Map<String, String> =
        clazz.declaredFields
                .filter { Modifier.isStatic(it.modifiers) && !Modifier.isPrivate(it.modifiers) && it.type == Int::class.javaPrimitiveType }
                .map {
                    val resourceName = it.name
                    try {
                        val resource = context.resources.getIdentifier(resourceName, "string", context.packageName)
                        resourceName to context.getString(resource)
                    } catch (e: Resources.NotFoundException) {
                        "" to ""
                    }
                }
                .filter { !it.first.isEmpty() || !it.second.isEmpty() }
                .toMap()

internal fun replaceTextRecursively(root: ViewGroup, process: (TextView) -> Unit = bindTextView) {
    (0 until root.childCount)
            .forEach { i ->
                val child = root.getChildAt(i)

                if (child is TextView) {
                    process.invoke(child)
                }

                if (child is ViewGroup) {
                    replaceTextRecursively(child, process)
                }
            }
}

internal val bindTextView: (TextView) -> Unit = { textView ->
    var isHint = false
    var targetKey: String? = null

    val value = keysValuesResources
            .filterValues {
                val text = textView.text.toString()
                var matches = (it == text && !text.isNullOrEmpty() && !it.isNullOrEmpty())
                if (!matches && textView is EditText) {
                    if (it == textView.hint) {
                        matches = true
                        isHint = true
                    }
                }
                matches
            }
            .map { it.key }
            .run {
                if (size != 1) {
                    null
                } else {
                    targetKey = get(0)
                    patches[get(0)]
                }
            }

    val textFallback = (textView as? EditText)?.text?.toString() ?: textView.text
    val text = (value ?: textFallback).addDebug(targetKey)

    if (isHint && textView is EditText) {
        textView.hint = text
    } else {
        textView.text = text
    }
}







