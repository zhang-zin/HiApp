package com.zj.hi_library.restful

open class HiResponse<T> {

    companion object {
        const val RC_NEED_LOGIN = -1001
        const val SUCCESS: Int = 0
        const val CACHE_SUCCESS = 304
    }

    var rawData: String? = null // 原始数据
    var code = 0 // 业务状态码
    var data: T? = null // 业务数据
    var errorData: Map<String, String>? = null // 错误状态下的数据

    var msg: String? = null // 错误信息
}