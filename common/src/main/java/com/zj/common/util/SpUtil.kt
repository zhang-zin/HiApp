package com.zj.common.util

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.zj.hi_library.util.AppGlobals

object SpUtil {

    private const val CACHE_FILE = "cache_file"

    private fun getShared(): SharedPreferences? {
        val application: Application? = AppGlobals.get()
        if (application != null) {
            return application.getSharedPreferences(CACHE_FILE, Context.MODE_PRIVATE)
        }
        return null
    }

    fun putString(key: String, value: String) {
        val shared = getShared()
        shared?.edit()?.putString(key, value)?.apply()
    }

    fun getString(key: String, defValue: String = ""): String? {
        val shared = getShared()
        if (shared != null) {
            return shared.getString(key, defValue)
        }
        return null
    }


    fun putBoolean(key: String, value: Boolean) {
        val shared = getShared()
        shared?.edit()?.putBoolean(key, value)?.apply()
    }

    fun getBoolean(key: String, defValue: Boolean = false): Boolean {
        val shared = getShared()
        if (shared != null) {
            return shared.getBoolean(key, defValue)
        }
        return false
    }


    fun putInt(key: String, value: Int) {
        val shared = getShared()
        shared?.edit()?.putInt(key, value)?.apply()
    }

    fun getInt(key: String, defValue: Int = 0): Int {
        val shared = getShared()
        if (shared != null) {
            return shared.getInt(key, defValue)
        }
        return 0
    }
}