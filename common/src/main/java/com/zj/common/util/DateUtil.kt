package com.zj.common.util

import java.text.SimpleDateFormat
import java.util.*

object DateUtil {
    private const val MD_FORMAT = "MM-dd"
    private const val DEFAULT_FORMAT = "yyyy-MM-dd HH:mm:ss"

    fun getMDData(date: Date): String {
        val sdf = SimpleDateFormat(MD_FORMAT, Locale.getDefault())
        return sdf.format(date)
    }

    fun getMDData(dataString: String): String {
        val sdf = SimpleDateFormat(DEFAULT_FORMAT, Locale.getDefault())
        return getMDData(sdf.parse(dataString)!!)
    }
}