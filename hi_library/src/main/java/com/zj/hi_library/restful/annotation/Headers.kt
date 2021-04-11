package com.zj.hi_library.restful.annotation

/**
 * @Headers({"connection:keep-alive","auth-token:token"})
 * fun test(@Filed("province") int provinceID)
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class Headers(vararg val value: String)
