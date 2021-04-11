package com.zj.hi_library.restful.annotation

/**
 * @GET("/cities/all")
 * fun test(@Filed("province") int provinceID)
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class GET(val value: String)