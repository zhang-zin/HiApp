package com.zj.hi_library.restful

import java.lang.reflect.Method
import java.lang.reflect.Proxy
import java.util.concurrent.ConcurrentHashMap

open class HiRestful constructor(
    private val baseUrl: String,
    callFactory: HiCall.Factory
) {

    private var interceptors: MutableList<HiInterceptor> = mutableListOf()
    private var methodParsers: ConcurrentHashMap<Method, MethodParser> = ConcurrentHashMap()
    private var scheduler: Scheduler

    init {
        scheduler = Scheduler(callFactory, interceptors)
    }

    fun addInterceptor(interceptor: HiInterceptor) {
        interceptors.add(interceptor)
    }

    fun <T> create(service: Class<T>): T {
        return Proxy.newProxyInstance(
            service.classLoader,
            arrayOf<Class<*>>(service)
        ) { proxy, method, args ->
            var methodParser = methodParsers[method]
            if (methodParser == null) {
                methodParser = MethodParser.parse(baseUrl, method, args)
                methodParsers[method] = methodParser
            }
            val request = methodParser.newRequest()
            //callFactory.newCall(request)
            scheduler.newCall(request)
        } as T
    }

}