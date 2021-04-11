package com.zj.hi_library.restful.annotation

/**
 * @BaseUrl("https://api.a.com/as/")
 * fun test(@Filed("province") int provinceID)
 */
@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class Filed(val value: String)
