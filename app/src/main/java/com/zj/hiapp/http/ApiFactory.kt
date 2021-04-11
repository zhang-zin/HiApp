package com.zj.hiapp.http

import com.zj.hi_library.restful.HiRestful

object ApiFactory {

    private const val baseUrl = "https://www.wanandroid.com"
    private val hiRestful: HiRestful = HiRestful(baseUrl, RetrofitCallFactory(baseUrl))

    init {
        hiRestful.addInterceptor(BizInterceptor())
    }

    fun <T> create(service: Class<T>): T {
        return hiRestful.create(service)
    }
}