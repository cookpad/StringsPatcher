package org.cookpad.strings_patcher.internal

import android.content.Context
import com.nhaarman.mockito_kotlin.mock
import org.assertj.core.api.Assertions
import org.json.JSONArray
import org.json.JSONObject
import org.junit.Assert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class SpreadsheetsTest {

    @Test fun verifySomething() {
        val a = JSONObject().apply {
            put("feed", JSONObject().apply {
                put("entry", JSONArray())
            })
        }
        Assertions.assertThat(a.getString("feed")).isEqualTo("")

        val context = mock<Context>()
        val block: (String) -> JSONObject = { _: String ->
            JSONObject().apply {
                put("feed", JSONObject().apply {
                    put("entry", JSONArray())
                })
            }
        }

        val actual = downloadPatches(null,
                "spread_sheet_key",
                "worksheet_name",
                "locale",
                context,
                block)

        Assertions.assertThat(actual).isEmpty()
    }
}

