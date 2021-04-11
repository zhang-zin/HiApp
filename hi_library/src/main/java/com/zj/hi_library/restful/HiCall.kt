package com.zj.hi_library.restful

import java.io.IOException
import kotlin.jvm.Throws

interface HiCall<T> {

    @Throws(IOException::class)
    fun execute(): HiResponse<T>

    fun enqueue(callback: HiCallback<T>)

    interface Factory {
        fun newCall(request: HiRequest): HiCall<*>
    }
}