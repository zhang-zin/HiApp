package com.zj.hi_debugtools

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class HiDebug(val name: String, val desc: String = "", val parameterType: Int = 0)
