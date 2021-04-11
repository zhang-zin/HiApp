package com.zj.hi_library.restful.annotation

/**
 * @BaseUrl("https://api.a.com/as/")
 * fun test(@Filed("province") int provinceID)
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class BaseUrl(val value: String)
