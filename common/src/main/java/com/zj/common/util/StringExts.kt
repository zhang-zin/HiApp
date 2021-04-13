package com.zj.common.util

import com.zj.hi_library.util.AppGlobals

fun String.Toast(duration: Int = android.widget.Toast.LENGTH_SHORT) {
    val application = AppGlobals.get()
    application?.let {
        android.widget.Toast.makeText(it, this, duration).show()
    }
}