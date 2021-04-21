package com.zj.hiapp.http.api

import com.zj.hi_library.restful.HiCall
import com.zj.hi_library.restful.annotation.CacheStrategy
import com.zj.hi_library.restful.annotation.GET
import com.zj.hiapp.http.model.CategoryModel

interface CategoryApi {

    @CacheStrategy(CacheStrategy.CACHE_FIRST)
    @GET("tree/json")
    fun getTreeJson(): HiCall<List<CategoryModel>>
}