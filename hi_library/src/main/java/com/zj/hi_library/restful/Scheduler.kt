package com.zj.hi_library.restful

import com.zj.hi_library.cache.HiStorage
import com.zj.hi_library.executor.HiExecutor
import com.zj.hi_library.hiLog.HiLog
import com.zj.hi_library.restful.annotation.CacheStrategy
import com.zj.hi_library.util.MainHandler
import java.util.concurrent.atomic.AtomicBoolean

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

    internal inner class ProxyCall<T>(private val delegate: HiCall<T>, val request: HiRequest) :
        HiCall<T> {

        var isCache = AtomicBoolean(false)

        override fun execute(): HiResponse<T> {
            dispatchInterceptor(request, null)
            val response = delegate.execute()
            dispatchInterceptor(request, response)
            return response
        }

        override fun enqueue(callback: HiCallback<T>) {
            dispatchInterceptor(request, null)
            HiLog.w("开始请求接口")
            if (request.cacheStrategy == CacheStrategy.CACHE_FIRST) {
                HiExecutor.execute(runnable = {
                    val readCache = readCache<T>()
                    if (readCache != null) {
                        HiLog.w("缓存获取成功")
                        isCache.set(true)
                        MainHandler.postAtFrontOfQueue(runnable = {
                            callback.onSuccess(readCache)
                        })
                    }
                })

            }
            delegate.enqueue(object : HiCallback<T> {
                override fun onSuccess(response: HiResponse<T>) {
                    dispatchInterceptor(request, response)
                    // isCache.get() == true 请求过缓存，这次网络请求只更新缓存
                    if (!isCache.get()) {
                        HiLog.w("使用接口返回的数据")
                        callback.onSuccess(response)
                    } else {
                        isCache.set(false)
                    }
                    if (request.cacheStrategy == CacheStrategy.CACHE_FIRST || request.cacheStrategy == CacheStrategy.NET_CACHE) {
                        // 更新缓存
                        HiLog.w("写入缓存")
                        writeCache(response)
                    }
                }

                override fun onFailed(throwable: Throwable) {
                    if (request.cacheStrategy == CacheStrategy.CACHE_FIRST || request.cacheStrategy == CacheStrategy.NET_CACHE) {
                        // 请求失败使用缓存
                        HiExecutor.execute(runnable = {
                            val readCache = readCache<T>()
                            HiLog.w("请求失败使用缓存")
                            MainHandler.postAtFrontOfQueue(runnable = {
                                if (readCache != null) {
                                    callback.onSuccess(readCache)
                                } else {
                                    callback.onFailed(throwable)
                                }
                            })
                        })
                    } else {
                        callback.onFailed(throwable)
                    }
                }
            })
        }

        private fun dispatchInterceptor(request: HiRequest, response: HiResponse<T>?) {
            InterceptorChain(request, response).dispatch()
        }

        private fun <T> readCache(): HiResponse<T>? {
            val cacheKey: String = request.getCacheKey()
            val cache = HiStorage.getCache<T>(cacheKey) ?: return null
            val cacheResponse = HiResponse<T>()
            cacheResponse.data = cache
            cacheResponse.code = HiResponse.CACHE_SUCCESS
            cacheResponse.msg = "缓存获取成功"
            return cacheResponse
        }

        private fun writeCache(response: HiResponse<T>) {
            HiExecutor.execute(runnable = {
                val cacheKey = request.getCacheKey()
                HiStorage.saveCache(cacheKey, response.data)
            })
        }

        //region 拦截器控制
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
        //endregion
    }
}