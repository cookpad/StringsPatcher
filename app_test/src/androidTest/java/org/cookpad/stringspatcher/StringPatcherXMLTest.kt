package org.cookpad.stringspatcher

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.rule.ActivityTestRule
import io.victoralbertos.device_animation_test_rule.DeviceAnimationTestRule
import org.junit.ClassRule
import org.junit.Rule
import org.junit.Test

class StringPatcherXMLTest {
    companion object {
        @ClassRule @JvmField var animRule = DeviceAnimationTestRule()
    }

    @Rule @JvmField var rule = ActivityTestRule(AppTestActivity::class.java)

    @Test fun verifyStringsPatcherFromXML() {
        Thread.sleep(3000)

        onView(withId(R.id.btRestartActivity)).perform(click())

        onView(withText("value1 Updated!")).check(matches(isDisplayed()))
        onView(withText("value2 Updated!")).check(matches(isDisplayed()))
        onView(withText("value3 Updated!")).check(matches(isDisplayed()))
        onView(withText("value4 Updated!")).check(matches(isDisplayed()))
        onView(withId(R.id.etWithHintUpdated)).check(matches(withHint("value5 Updated!")))
        onView(withId(R.id.etWithHintWithoutPatch)).check(matches(withHint("Value Without Patch")))

        onView(withId(R.id.btStringPatcherDebugEnabled)).perform(click())
        onView(withId(R.id.btRestartActivity)).perform(click())

        onView(withText("value1  📝  value1 Updated!")).check(matches(isDisplayed()))
        onView(withText("value2  📝  value2 Updated!")).check(matches(isDisplayed()))
        onView(withText("value3  📝  value3 Updated!")).check(matches(isDisplayed()))
        onView(withText("value4  📝  value4 Updated!")).check(matches(isDisplayed()))
        onView(withId(R.id.etWithHintUpdated)).check(matches(withHint("value5  📝  value5 Updated!")))
        onView(withId(R.id.etWithHintWithoutPatch)).check(matches(withHint("valueWithoutPatch  📝  Value Without Patch")))
    }
}