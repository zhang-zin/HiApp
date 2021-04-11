package com.zj.hiapp.http.api

import com.zj.hi_library.restful.HiCall
import com.zj.hi_library.restful.annotation.GET

interface WanAndroid {

    @GET("/banner/json")
    fun getBanner(v:String): HiCall<String>
}