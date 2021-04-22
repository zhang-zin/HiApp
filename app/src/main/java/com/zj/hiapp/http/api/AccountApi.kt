package com.zj.hiapp.http.api

import com.zj.hi_library.restful.HiCall
import com.zj.hi_library.restful.annotation.CacheStrategy
import com.zj.hi_library.restful.annotation.Filed
import com.zj.hi_library.restful.annotation.GET
import com.zj.hi_library.restful.annotation.POST
import com.zj.hiapp.http.model.CoinInfoModel
import com.zj.hiapp.http.model.UserModel

interface AccountApi {

    @POST("user/login")
    fun login(
        @Filed("username") username: String,
        @Filed("password") password: String
    ): HiCall<UserModel>

    @POST("user/register")
    fun register(
        @Filed("username") username: String,
        @Filed("password") password: String,
        @Filed("repassword") repassword: String
    ): HiCall<UserModel>

    @GET("user/logout/json")
    fun logout(): HiCall<String>

    @CacheStrategy(CacheStrategy.CACHE_FIRST)
    @GET("lg/coin/userinfo/json")
    fun getCoinInfo(): HiCall<CoinInfoModel>

}