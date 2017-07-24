package org.cookpad.stringspatcher

import android.R
import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.view.ViewGroup
import org.cookpad.strings_patcher.bindStringsPatchers


class LifecycleObserver : Application.ActivityLifecycleCallbacks {

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        //Exists to satisfy the contract.
    }

    override fun onActivityPaused(activity: Activity) {
        //Exists to satisfy the contract.
    }

    override fun onActivityResumed(activity: Activity) {
        //Exists to satisfy the contract.
    }

    override fun onActivityStarted(activity: Activity) {
        val viewGroup = (activity.findViewById(R.id.content) as ViewGroup).getChildAt(0) as ViewGroup
        bindStringsPatchers(viewGroup)
    }

    override fun onActivityDestroyed(activity: Activity) {
        //Exists to satisfy the contract.
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
        //Exists to satisfy the contract.
    }

    override fun onActivityStopped(activity: Activity) {
        //Exists to satisfy the contract.
    }
}