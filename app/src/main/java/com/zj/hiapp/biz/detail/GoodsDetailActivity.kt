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
import com.zj.hiapp.databinding.ActivityGoodsDetailBinding
import com.zj.hiapp.http.ApiFactory
import com.zj.hiapp.http.api.GoodsApi
import com.zj.hiapp.http.model.GoodDetailModel
import com.zj.hiapp.router.HiRoute

@Route(path = "/detail/main")
class GoodsDetailActivity : HiBaseActivity<ActivityGoodsDetailBinding>() {

    lateinit var goodDetailViewModel: GoodDetailViewModel

    @Autowired
    lateinit var goodsSign: String

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
            if (goodsDetail != null) {
                bindData(goodsDetail)
            }
        }

    }

    private fun bindData(goodsDetail: GoodDetailModel) {
        val hiAdapter = binding.goodDetailList.adapter as HiAdapter
        val dataItems = mutableListOf<HiDataItem<*, *>>()
        val goodsDetail = goodsDetail.goods_detail_response.goods_details[0]
        dataItems.add(
            HeaderItem(
                goodsDetail.goods_gallery_urls,
                goodsDetail.min_group_price.toString(),
                goodsDetail.sales_tip,
                goodsDetail.goods_name
            )
        )
        hiAdapter.clearItems()
        hiAdapter.addItems(dataItems, true)
    }

}