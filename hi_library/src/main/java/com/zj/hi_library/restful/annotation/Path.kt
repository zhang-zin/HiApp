package com.zj.hi_library.restful.annotation

/**
 * @GET("/cities/all")
 * fun test(@Path("province") int provinceID)
 */
@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class Path(val value:String)
