package com.zj.hiapp.http.model

data class GoodDetailModel(
    val goods_detail_response: GoodsDetailResponse
)

data class GoodsList(
    val goods_basic_detail_response: GoodsListResponse
)

data class GoodsListResponse(
    val list: List<GoodsDetail>,
    val request_id: String,
    val search_id: String
)

data class GoodsDetailResponse(
    val goods_details: List<GoodsDetail>,
    val request_id: String
)

data class GoodsDetail(
    val cat_ids: List<Int>,
    val category_id: Int,
    val category_name: String,
    val coupon_discount: Int,
    val coupon_end_time: Int,
    val coupon_min_order_amount: Int,
    val coupon_remain_quantity: Int,
    val coupon_start_time: Int,
    val coupon_total_quantity: Int,
    val desc_txt: String,
    val goods_desc: String,
    val goods_gallery_urls: List<String>,
    val goods_id: Long,
    val goods_image_url: String,
    val goods_name: String,
    val goods_sign: String,
    val goods_thumbnail_url: String,
    val has_coupon: Boolean,
    val has_mall_coupon: Boolean,
    val lgst_txt: String,
    val mall_coupon_discount_pct: Int,
    val mall_coupon_end_time: Int,
    val mall_coupon_max_discount_amount: Int,
    val mall_coupon_min_order_amount: Int,
    val mall_coupon_remain_quantity: Int,
    val mall_coupon_start_time: Int,
    val mall_coupon_total_quantity: Int,
    val mall_cps: Int,
    val mall_id: Int,
    val mall_img_url: String,
    val mall_name: String,
    val merchant_type: Int,
    val min_group_price: Int,
    val min_normal_price: Int,
    val only_scene_auth: Boolean,
    val opt_id: Int,
    val opt_ids: List<Int>,
    val opt_name: String,
    val plan_type: Int,
    val predict_promotion_rate: Int,
    val promotion_rate: Int,
    val sales_tip: String,
    val serv_txt: String,
    val service_tags: List<Int>,
    val share_rate: Int,
    val unified_tags: List<String>,
    val video_urls: List<Any>,
    val zs_duo_id: Int
)