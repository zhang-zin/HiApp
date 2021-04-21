package com.zj.hi_library.restful

import android.text.TextUtils
import androidx.annotation.IntDef
import com.zj.hi_library.restful.annotation.CacheStrategy
import java.lang.IllegalStateException
import java.lang.reflect.Type
import java.net.URLEncoder

open class HiRequest {

    private var cacheStrategyKey: String = ""

    @METHOD
    var httpMethod: Int = 0 // 请求方式GET，POST
    var headers: MutableMap<String, String>? = null // 是否表单提交，POST有效
    var parameters: MutableMap<String, String>? = null // 请求入参
    var domainUrl: String? = null // 域名
    var relativeUrl: String? = null // 相对路径
    var returnType: Type? = null // restful方法的泛型返回值
    var formPost: Boolean = true
    var cacheStrategy = CacheStrategy.NET_ONLY

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

    fun getCacheKey(): String {
        if (!TextUtils.isEmpty(cacheStrategyKey)) {
            return cacheStrategyKey
        }
        val builder = StringBuilder()
        val endPointUrl = endPointUrl()
        builder.append(endPointUrl)
        if (endPointUrl.indexOf("?") > 0 || endPointUrl.indexOf("&") > 0) {
            builder.append("&")
        } else {
            builder.append("?")
        }
        if (parameters != null) {
            for ((key, value) in parameters!!) {
                try {
                    val encode = URLEncoder.encode(value, "UTF-8")
                    builder.append(key).append("=").append(encode).append("&")
                } catch (e: Exception) {
                    //ignore
                }
            }
            builder.deleteCharAt(builder.length - 1)
        }
        cacheStrategyKey = builder.toString()
        return cacheStrategyKey
    }
}
