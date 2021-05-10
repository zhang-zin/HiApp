package com.zj.hi_library.util

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat

object HiRes {

    fun context(): Context {
        return AppGlobals.get() as Context
    }

    fun getString(@StringRes id: Int): String {
        return context().getString(id)
    }

    fun getString(@StringRes id: Int, vararg formatArgs: Any?): String {
        return context().getString(id, formatArgs)
    }

    fun getColor(@ColorRes id: Int): Int {
        return ContextCompat.getColor(context(), id)
    }

    fun getDrawable(@DrawableRes drawableId: Int): Drawable? {
        return ContextCompat.getDrawable(context(), drawableId)
    }
}