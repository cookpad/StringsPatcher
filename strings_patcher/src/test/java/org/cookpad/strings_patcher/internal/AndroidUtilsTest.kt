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
import android.text.Editable
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.nhaarman.mockito_kotlin.*
import org.assertj.core.api.Assertions.assertThat
import org.cookpad.strings_patcher.keysValuesResources
import org.cookpad.strings_patcher.patches
import org.junit.Test

class AndroidUtilsTest {

    @Test fun verifyGetAllKeysValuesResources() {
        val context = mock<Context>()
        whenever(context.getString(any())).thenReturn("no matter")

        val resources = mock<Resources>()
        whenever(context.resources).thenReturn(resources)

        val keysValues = getAllKeysValuesResources(R.string::class.java, context)
        assertThat(keysValues).hasSize(4)

        val keys = keysValues.map { it.key }
        assertThat(keys[0]).isEqualTo("abc_action_bar_home_description")
        assertThat(keys[1]).isEqualTo("abc_action_bar_home_description_format")
        assertThat(keys[2]).isEqualTo("abc_action_bar_home_subtitle_description_format")
        assertThat(keys[3]).isEqualTo("abc_action_bar_up_description")
    }

    @Test fun verifyTraverseView() {
        val root = mock<ViewGroup>()
        val textViewAtRoot = mock<TextView>()
        val buttonAtRoot = mock<Button>()
        val viewAtRoot = mock<View>()

        val nested = mock<ViewGroup>()
        val textViewAtNested = mock<TextView>()
        val buttonAtNested = mock<Button>()
        val viewAtNested = mock<View>()

        whenever(root.childCount).thenReturn(4)
        whenever(root.getChildAt(0)).thenReturn(nested)
        whenever(root.getChildAt(1)).thenReturn(textViewAtRoot)
        whenever(root.getChildAt(2)).thenReturn(buttonAtRoot)
        whenever(root.getChildAt(3)).thenReturn(viewAtRoot)

        whenever(nested.childCount).thenReturn(3)
        whenever(nested.getChildAt(0)).thenReturn(textViewAtNested)
        whenever(nested.getChildAt(1)).thenReturn(buttonAtNested)
        whenever(nested.getChildAt(2)).thenReturn(viewAtNested)

        val calledTextViews = mutableListOf<TextView>()
        traverseView(root, { calledTextViews.add(it) })

        assertThat(calledTextViews).hasSize(4)
        assertThat(calledTextViews[0]).isEqualTo(textViewAtNested)
        assertThat(calledTextViews[1]).isEqualTo(buttonAtNested)
        assertThat(calledTextViews[2]).isEqualTo(textViewAtRoot)
        assertThat(calledTextViews[3]).isEqualTo(buttonAtRoot)
    }

    @Test fun verifyBindTextView() {
        val val1 = "val1"
        val val2 = "val2"
        val val3 = "val3"

        val path1 = "path1"
        val path2 = "path2"
        val path3 = "path3"

        val key1 = "key1"
        val key2 = "key2"
        val key3 = "key3"

        keysValuesResources = mapOf(key1 to val1, key2 to val2, key3 to val3)
        patches = mapOf(key1 to path1, key2 to path2, key3 to path3)

        val tv1 = mock<TextView>()
        whenever(tv1.text).thenReturn(val1)
        bindTextView(tv1)
        verify(tv1, times(1)).text = path1

        val tv2 = mock<TextView>()
        whenever(tv2.text).thenReturn(val2)
        bindTextView(tv2)
        verify(tv2, times(1)).text = path2

        val tv3 = mock<TextView>()
        whenever(tv3.text).thenReturn(val3)
        bindTextView(tv3)
        verify(tv3, times(1)).text = path3

        val tv4 = mock<TextView>()
        whenever(tv4.text).thenReturn("no matches")
        bindTextView(tv4)
        verify(tv4, never()).text = any()

        val et1 = mock<EditText>()
        val editable1 = mock<Editable>()
        whenever(et1.text).thenReturn(editable1)
        whenever(editable1.toString()).thenReturn(val1)
        bindTextView(et1)
        verify(et1 as TextView, times(1)).text = path1

        val et2 = mock<EditText>()
        val editable2 = mock<Editable>()
        whenever(et2.text).thenReturn(editable2)
        whenever(editable2.toString()).thenReturn("no matches")
        whenever(et2.hint).thenReturn(val2)
        bindTextView(et2)
        verify(et2, times(1)).hint = path2
    }

}
