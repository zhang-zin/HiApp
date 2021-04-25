package com.zj.hiapp.http.api

import com.zj.hi_library.restful.HiCall
import com.zj.hi_library.restful.annotation.BaseUrl
import com.zj.hi_library.restful.annotation.Filed
import com.zj.hi_library.restful.annotation.GET
import com.zj.hiapp.http.model.GoodDetailModel

interface GoodsApi {

//https://gw-api.pinduoduo.com/api/router?
// type=pdd.ddk.goods.detail
// &data_type=JSON
// &client_id=28392be21c914c7fa4a077a2a9d338c7
// &goods_sign=Y9z2luxNyt1Uv1IxwfDZeoUikQeR7J48_JQ9SyNG6Fj
// &timestamp=1619270536
// &sign=54D945C3BA5D9120A256FFEEC4FCC3D3

    @BaseUrl("https://gw-api.pinduoduo.com/")
    @GET("api/router")
    fun getGoodsDetail(
        @Filed("type") type: String = "pdd.ddk.goods.detail",
        @Filed("data_type") data_type: String = "JSON",
        @Filed("client_id") client_id: String = "28392be21c914c7fa4a077a2a9d338c7",
        @Filed("goods_sign") goods_sign: String = "Y9z2luxNyt1Uv1IxwfDZeoUikQeR7J48_JQ9SyNG6Fj",
        @Filed("timestamp") timestamp: String = "1619270536",
        @Filed("sign") sign: String = "54D945C3BA5D9120A256FFEEC4FCC3D3"
    ): HiCall<GoodDetailModel>
}