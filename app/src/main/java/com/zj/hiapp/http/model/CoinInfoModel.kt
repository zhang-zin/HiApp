package com.zj.hiapp.http.model

import java.io.Serializable

data class CoinInfoModel(
    val coinCount: Int,
    val level: Int,
    val nickname: String,
    val rank: String,
    val userId: Int,
    val username: String
) : Serializable