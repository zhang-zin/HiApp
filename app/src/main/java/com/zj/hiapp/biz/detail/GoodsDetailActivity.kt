package com.zj.hiapp.biz.detail

import android.graphics.Color
import androidx.recyclerview.widget.GridLayoutManager
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.zj.common.ui.component.HiBaseActivity
import com.zj.hi_library.util.HiStatusBar
import com.zj.hi_ui.ui.hiitem.HiAdapter
import com.zj.hi_ui.ui.hiitem.HiDataItem
import com.zj.hiapp.R
import com.zj.hiapp.biz.detail.item.AppriseItem
import com.zj.hiapp.biz.detail.item.HeaderItem
import com.zj.hiapp.biz.detail.item.ShopItem
import com.zj.hiapp.databinding.ActivityGoodsDetailBinding
import com.zj.hiapp.http.model.GoodDetailModel
import com.zj.hiapp.http.model.GoodsDetail
import com.zj.hiapp.router.HiRoute
import java.util.ArrayList
import kotlin.random.Random

@Route(path = "/detail/main")
class GoodsDetailActivity : HiBaseActivity<ActivityGoodsDetailBinding>() {

    lateinit var goodDetailViewModel: GoodDetailViewModel

    @Autowired
    lateinit var goodsSign: String

    val appriseCount = Random(10).nextInt(1000)
    val appriseDesc = arrayOf("收到货，物流很快！包装完好无损，让我很满意。五星好评", "很好看", "质量很好，穿着很舒服")

    override fun getLayoutId() = R.layout.activity_goods_detail

    override fun setStatusBar() {
        HiStatusBar.setStatusBar(this, true, Color.TRANSPARENT, true)
    }

    override fun init() {
        HiRoute.inject(this)
        goodDetailViewModel = GoodDetailViewModel.get(this, goodsSign)
        binding.goodDetailList.layoutManager = GridLayoutManager(this, 2)
        binding.goodDetailList.adapter = HiAdapter(this)
        // preBindData() 轮播图可以预渲染
    }

    override fun initEvent() {
        goodDetailViewModel.getGoodsDetail(goodsSign).observe(this) { goodsDetail ->
            if (goodsDetail?.goods_detail_response != null) {
                bindData(goodsDetail)
            }
        }

    }

    private fun bindData(goodsDetail: GoodDetailModel) {
        val hiAdapter = binding.goodDetailList.adapter as HiAdapter
        val dataItems = mutableListOf<HiDataItem<*, *>>()
        val goodsDetail = goodsDetail.goods_detail_response.goods_details[0]
        hiAdapter.clearItems()

        // 详情页header item
        dataItems.add(
            HeaderItem(
                goodsDetail.goods_gallery_urls,
                goodsDetail.min_group_price.toString(),
                goodsDetail.sales_tip,
                goodsDetail.goods_name
            )
        )
        // 评价item
        dataItems.add(
            AppriseItem(
                appriseCount,
                goodsDetail.unified_tags.toTypedArray(),
                appriseDesc
            )
        )

        // 店铺item
        // goodsDetail.serv_txt 服务分, lgst_txt 物流分
        val shopDescNumber =
            arrayOf(
                "描述相符 " + goodsDetail.desc_txt,
                "服务态度 " + goodsDetail.serv_txt,
                "物流服务 " + goodsDetail.lgst_txt
            )

        val list = ArrayList<GoodsDetail>()
        for (index in 0..5) {
            list.add(goodDetailViewModel.goodsListModel.value?.goods_basic_detail_response?.list!![index])
        }

        dataItems.add(
            ShopItem(
                goodsDetail.mall_name,
                goodsDetail.mall_img_url,
                goodsDetail.sales_tip,
                shopDescNumber,
                list
            )
        )
        hiAdapter.addItems(dataItems, true)
    }

}