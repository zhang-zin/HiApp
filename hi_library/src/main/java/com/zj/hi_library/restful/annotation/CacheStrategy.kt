package com.zj.hi_library.restful.annotation

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.VALUE_PARAMETER)
annotation class CacheStrategy(val value: Int = NET_ONLY) {
    companion object {
        const val CACHE_FIRST = 0 //请求接口的时候先读取本地缓存，再读取接口，接口成功后更新缓存（页面初始化数据）
        const val NET_ONLY = 1 //只请求接口（一般是分页和独立非列表页）
        const val NET_CACHE = 2 //先接口，接口成功后更新缓存（下拉刷新）
    }
}