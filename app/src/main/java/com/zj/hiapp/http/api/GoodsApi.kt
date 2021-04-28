package com.zj.hiapp.http.api

import com.zj.hi_library.restful.HiCall
import com.zj.hi_library.restful.annotation.BaseUrl
import com.zj.hi_library.restful.annotation.Filed
import com.zj.hi_library.restful.annotation.GET
import com.zj.hiapp.http.model.GoodDetailModel
import com.zj.hiapp.http.model.GoodsList

interface GoodsApi {

    //https://gw-api.pinduoduo.com/api/router
    // ?type=pdd.ddk.goods.detail
    // &data_type=JSON
    // &client_id=28392be21c914c7fa4a077a2a9d338c7
    // &goods_sign=Y9_2lBaTmphUv1I1wfvZdnv7XsT2T9pM_JQy0p6JSfO
    // &timestamp=1619612799
    // &sign=ADF9E83631A63E90142519CB58A0D7B4

    @BaseUrl("https://gw-api.pinduoduo.com/")
    @GET("api/router")
    fun getGoodsDetail(
        @Filed("type") type: String = "pdd.ddk.goods.detail",
        @Filed("data_type") data_type: String = "JSON",
        @Filed("client_id") client_id: String = "28392be21c914c7fa4a077a2a9d338c7",
        @Filed("goods_sign") goods_sign: String = "Y9_2lBaTmphUv1I1wfvZdnv7XsT2T9pM_JQy0p6JSfO",
        @Filed("timestamp") timestamp: String = "1619612799",
        @Filed("sign") sign: String = "ADF9E83631A63E90142519CB58A0D7B4"
    ): HiCall<GoodDetailModel>

    //https://gw-api.pinduoduo.com/api/router?
    // type=pdd.ddk.goods.recommend.get
    // &data_type=JSON
    // &client_id=28392be21c914c7fa4a077a2a9d338c7
    // &timestamp=1619611833
    // &sign=4C4A3279533673BB6733DA6BEDC57188
    @BaseUrl("https://gw-api.pinduoduo.com/")
    @GET("api/router")
    fun getGoodsRecommend(
        @Filed("type") type: String = "pdd.ddk.goods.recommend.get",
        @Filed("data_type") data_type: String = "JSON",
        @Filed("client_id") client_id: String = "28392be21c914c7fa4a077a2a9d338c7",
        @Filed("timestamp") timestamp: String = "1619611833",
        @Filed("sign") sign: String = "4C4A3279533673BB6733DA6BEDC57188"
    ): HiCall<GoodsList>
}