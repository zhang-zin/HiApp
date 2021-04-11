package com.zj.hi_library.restful.annotation

/**
 * @POST("/cities/all")
 * fun test(@Filed("province") int provinceID)
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class POST(val value: String, val formPost: Boolean = true)
