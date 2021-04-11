package com.zj.hi_library.restful

interface HiInterceptor {

    fun intercept(chain: Chain): Boolean

    /**
     * Chain对象在派发拦截器的时候创建
     */
    interface Chain {

        val isRequestPeriod: Boolean get() = false

        fun request(): HiRequest

        /**
         * 这个response对象，在请求网络发起之前是为空的
         */
        fun response(): HiResponse<*>?
    }
}