package com.zj.hi_library.util

import android.os.Handler
import android.os.Looper

object MainHandler {

    private val handler = Handler(Looper.getMainLooper())

    fun post(runnable: Runnable) {
        handler.post(runnable)
    }

    fun postDelayed(runnable: Runnable, delayMillis: Long) {
        handler.postDelayed(runnable, delayMillis)
    }

    fun postAtFrontOfQueue(runnable: Runnable) {
        handler.postAtFrontOfQueue(runnable)
    }
}