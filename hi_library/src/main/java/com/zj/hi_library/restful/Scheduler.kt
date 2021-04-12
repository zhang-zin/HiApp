package com.zj.hi_library.restful

/**
 * 代理call对象，从而实现拦截器的分发
 */
class Scheduler(
    private val callFactory: HiCall.Factory,
    val interceptors: MutableList<HiInterceptor>
) {
    fun newCall(request: HiRequest): HiCall<*> {
        val newCall = callFactory.newCall(request)
        return ProxyCall(newCall, request)
    }

    internal inner class ProxyCall<T>(private val delegate: HiCall<T>, val request: HiRequest) : HiCall<T> {
        override fun execute(): HiResponse<T> {
            dispatchInterceptor(request, null)
            val response = delegate.execute()
            dispatchInterceptor(request, response)
            return response
        }

        override fun enqueue(callback: HiCallback<T>) {
            dispatchInterceptor(request, null)
            delegate.enqueue(object : HiCallback<T> {
                override fun onSuccess(response: HiResponse<T>) {
                    dispatchInterceptor(request, response)
                    callback.onSuccess(response)
                }

                override fun onFailed(throwable: Throwable) {
                    callback.onFailed(throwable)
                }
            })
        }

        private fun dispatchInterceptor(request: HiRequest, response: HiResponse<T>?) {
            InterceptorChain(request, response).dispatch()
        }

        internal inner class InterceptorChain(
            private val request: HiRequest,
            private val response: HiResponse<T>?
        ) : HiInterceptor.Chain {

            var callIndex: Int = 0

            override val isRequestPeriod: Boolean
                get() = response == null

            override fun request(): HiRequest {
                return request
            }

            override fun response(): HiResponse<*>? {
                return response
            }

            fun dispatch() {
                val hiInterceptor = interceptors[callIndex]
                val intercept = hiInterceptor.intercept(this)
                callIndex++
                if (!intercept && callIndex < interceptors.size) {
                    dispatch()
                }
            }
        }
    }
}