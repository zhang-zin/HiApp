package com.zj.hiapp.http

import com.zj.hi_library.restful.HiInterceptor
import com.zj.hi_library.restful.HiResponse
import com.zj.hiapp.router.HiRoute


/**
 * 根据response 的 code 自动路由到相关页面
 */
class HttpStatusInterceptor : HiInterceptor {
    override fun intercept(chain: HiInterceptor.Chain): Boolean {
        val response = chain.response();
        if (!chain.isRequestPeriod && response != null) {
            val code = response.code
            when (code) {
                HiResponse.RC_NEED_LOGIN -> {
                    HiRoute.startActivity(null,destination = HiRoute.Destination.ACCOUNT_LOGIN)
                }

            }
        }
        return false
    }

}