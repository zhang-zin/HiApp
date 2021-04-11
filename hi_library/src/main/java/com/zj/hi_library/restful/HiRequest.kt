package com.zj.hi_library.restful

import androidx.annotation.IntDef
import java.lang.IllegalStateException
import java.lang.reflect.Type

open class HiRequest {

    @METHOD
    var httpMethod: Int = 0 // 请求方式GET，POST
    var headers: MutableMap<String, String>? = null // 是否表单提交，POST有效
    var parameters: MutableMap<String, String>? = null // 请求入参
    var domainUrl: String? = null // 域名
    var relativeUrl: String? = null // 相对路径
    var returnType: Type? = null // restful方法的泛型返回值
    var formPost: Boolean = true

    @IntDef(value = [METHOD.GET, METHOD.POST])
    annotation class METHOD {
        companion object {
            const val GET = 0
            const val POST = 1
        }
    }

    fun endPointUrl(): String {
        if (relativeUrl.isNullOrEmpty()) {
            throw IllegalStateException("relative url must bot be null")
        }

        if (!relativeUrl!!.startsWith("/")) {
            return domainUrl + relativeUrl
        }

        //https://api.devio.org/v1/ ---relativeUrl: /v2/user/login===>https://api.devio.org/v2/user/login
        val indexOf = domainUrl!!.indexOf("/") // ?有问题
        return domainUrl!!.substring(0, indexOf) + relativeUrl
    }

    fun addHeader(key: String, value: String) {
        if (headers == null) {
            headers = mutableMapOf()
        }
        headers!![key] = value
    }
}
