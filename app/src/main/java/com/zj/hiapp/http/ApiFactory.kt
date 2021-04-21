package com.zj.hiapp.http

import com.zj.common.SP_KEY_DEGRADE_HTTP
import com.zj.common.util.SpUtil
import com.zj.hi_library.restful.HiRestful

object ApiFactory {

    private val HTTP_BASE_URL = "http://www.wanandroid.com/"
    private val HTTPS_BASE_URL = "https://www.wanandroid.com/"

    private val isDegrade = SpUtil.getBoolean(SP_KEY_DEGRADE_HTTP)
    private val baseUrl = if (false) HTTP_BASE_URL else HTTPS_BASE_URL

    private val hiRestful: HiRestful = HiRestful(baseUrl, RetrofitCallFactory(baseUrl))

    init {
        hiRestful.addInterceptor(BizInterceptor())
        hiRestful.addInterceptor(HttpStatusInterceptor())
        SpUtil.putBoolean(SP_KEY_DEGRADE_HTTP, false)
    }

    fun <T> create(service: Class<T>): T {
        return hiRestful.create(service)
    }
}