package com.zj.hi_library.util

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry

/**
 * 监听前台和后台的切换
 * 只分发了onStart 和 onStop状态
 * onStart表示后台切回了前台
 * onStop 前台进入后台
 */
object AppLifecycleOwner : LifecycleOwner {

    private val registry = LifecycleRegistry(this)
    private var activityStartCount = 0
    private var front = true

    override fun getLifecycle() = registry

    fun init(application: Application) {
        application.registerActivityLifecycleCallbacks(object :
            Application.ActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {

            }

            override fun onActivityStarted(activity: Activity) {
                activityStartCount++
                // activityStartCount > 0 说明当前处于可见状态，也就是前台
                // front = false 之前不是在前台
                if (activityStartCount > 0 && !front) {
                    front = true // 后台切前台
                    lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_START)
                }
            }

            override fun onActivityResumed(activity: Activity) {
            }

            override fun onActivityPaused(activity: Activity) {
            }

            override fun onActivityStopped(activity: Activity) {
                activityStartCount--
                if (activityStartCount <= 0 && front) {
                    front = true
                    lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_STOP)
                }
            }

            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
            }

            override fun onActivityDestroyed(activity: Activity) {
            }

        })
    }
}