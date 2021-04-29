package com.zj.hiapp.biz.detail.item

import android.view.LayoutInflater
import android.view.View
import androidx.core.view.get
import com.zj.common.ui.view.InputItemLayout
import com.zj.hi_ui.ui.hiitem.HiDataItem
import com.zj.hi_ui.ui.hiitem.HiViewHolder
import com.zj.hiapp.R
import com.zj.hiapp.databinding.LayoutDetailItemGoodsDetailBinding
import com.zj.hiapp.http.model.GoodsDetail

class GoodsDetailItem(private val goodsDetail: GoodsDetail) :
    HiDataItem<GoodsDetail, HiViewHolder>() {

    var binding: LayoutDetailItemGoodsDetailBinding? = null

    override fun onBindData(holder: HiViewHolder, position: Int) {
        if (binding == null) {
            binding = holder.bindingView()
        }
        binding?.run {

            for (index in 0..5) {
                llGoodsDetailItem.visibility = View.VISIBLE

                val item = if (index < llGoodsDetailItem.childCount) {
                    llGoodsDetailItem[index] as InputItemLayout
                } else {
                    val inputItemLayout = LayoutInflater.from(root.context)
                        .inflate(
                            R.layout.layout_good_detail_item,
                            llGoodsDetailItem,
                            false
                        ) as InputItemLayout
                    inputItemLayout.getEditText().isEnabled = false
                    llGoodsDetailItem.addView(inputItemLayout)
                    inputItemLayout
                }

                item.getEditText().setText("每天有面包")
                item.getTitleText().text = "品牌"
            }

            if (goodsDetail.goods_desc.isNotEmpty()) {
                tvGoodsDesc.visibility = View.VISIBLE
                tvGoodsDesc.text = goodsDetail.goods_desc
            }
        }

    }

    override fun getItemLayoutRes() = R.layout.layout_detail_item_goods_detail
}