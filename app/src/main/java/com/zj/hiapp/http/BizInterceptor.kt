package com.zj.hiapp.http

import com.zj.hi_library.hiLog.HiLog
import com.zj.hi_library.restful.HiInterceptor
import com.zj.hi_library.restful.HiRequest
import java.lang.StringBuilder

class BizInterceptor : HiInterceptor {
    override fun intercept(chain: HiInterceptor.Chain): Boolean {
        val request = chain.request()
        val response = chain.response()
        if (chain.isRequestPeriod) {
            // 可以添加header
        } else if (chain.response() != null) {
            val outputBuilder = StringBuilder()
            val httpMethod = if (request.httpMethod == HiRequest.METHOD.GET) "GET" else "POST"
            val requestUrl = request.endPointUrl()
            outputBuilder.append("\n$requestUrl==>$httpMethod\n")

            request.headers?.let {
                outputBuilder.appendLine("【headers")
                it.forEach(action = { header ->
                    outputBuilder.appendLine(header.key + ":" + header.value)
                })
                outputBuilder.appendLine("headers】")
            }

            request.parameters?.let {
                outputBuilder.appendLine("【parameters==>")
                it.forEach(action = { parameter ->
                    outputBuilder.appendLine(parameter.key + ":" + parameter.value)
                })
                outputBuilder.appendLine("parameters】")
            }

            outputBuilder.append("【response==>\n")
            outputBuilder.append(response!!.rawData + "\n")
            outputBuilder.append("response】\n")

            HiLog.eT("wanAndroid",outputBuilder.toString())
        }
        return false
    }
}