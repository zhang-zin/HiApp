package com.zj.hiapp.biz.detail.item

import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zj.common.ui.view.loadUrl
import com.zj.hi_ui.ui.hiitem.HiAdapter
import com.zj.hi_ui.ui.hiitem.HiDataItem
import com.zj.hi_ui.ui.hiitem.HiViewHolder
import com.zj.hiapp.R
import com.zj.hiapp.databinding.LayoutDetailItemShopBinding
import com.zj.hiapp.databinding.LayoutShopGoodsItemBinding
import com.zj.hiapp.fragment.GoodsItem
import com.zj.hiapp.http.model.GoodDetailModel
import com.zj.hiapp.http.model.GoodsDetail

class ShopItem(
    private val mallName: String,
    private val mallImgUrl: String,
    private val salesTip: String,
    private val shopDescNumber: Array<String>,
    private val shopGoodsList: MutableList<GoodsDetail>
) :
    HiDataItem<GoodDetailModel, HiViewHolder>() {

    var binding: LayoutDetailItemShopBinding? = null

    override fun getItemLayoutRes() = R.layout.layout_detail_item_shop

    override fun onBindData(holder: HiViewHolder, position: Int) {
        if (binding == null) {
            binding = holder.bindingView()
        }
        binding?.run {
            ivShopIcon.loadUrl(mallImgUrl)
            tvShopName.text = mallName
            tvShopDesc.text = String.format("商品数量6      已拼：%s件", salesTip)

            val context = root.context
            if (!shopDescNumber.isNullOrEmpty()) {
                llShopTag.visibility = View.VISIBLE
                for (index in shopDescNumber.indices) {
                    val tag = if (index < llShopTag.childCount) {
                        llShopTag.getChildAt(index) as TextView
                    } else {
                        val tagText = TextView(context)
                        val params =
                            LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT)
                        params.weight = 1f
                        tagText.gravity = Gravity.CENTER
                        tagText.layoutParams = params
                        llShopTag.addView(tagText)
                        tagText
                    }
                    tag.text = getTagSpannable(shopDescNumber[index])
                }
            }

            initShopGoodsData(rvShopGoods)
        }
    }

    private fun getTagSpannable(tagString: String): SpannableStringBuilder {
        val split = tagString.split(" ")
        val ssb = SpannableStringBuilder()
        val spannableString = SpannableString(split[1])
        spannableString.setSpan(
            ForegroundColorSpan(Color.parseColor("#DD2F24")),
            0,
            spannableString.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        ssb.append(split[0] + " ")
        ssb.append(spannableString)
        return ssb
    }

    private fun initShopGoodsData(rvShopGoods: RecyclerView) {
        if (rvShopGoods.layoutManager == null) {
            rvShopGoods.layoutManager = GridLayoutManager(rvShopGoods.context, 3)
        }

        if (rvShopGoods.adapter == null) {
            rvShopGoods.adapter = HiAdapter(rvShopGoods.context)
        }

        val dataItems = mutableListOf<GoodsItem>()
        shopGoodsList.forEach { _ ->
            dataItems.add(ShopGoodsItem(rvShopGoods))
        }
        val adapter = rvShopGoods.adapter as HiAdapter
        adapter.clearItems()
        adapter.addItems(dataItems, true)
    }

    inner class ShopGoodsItem(val rvShopGoods: RecyclerView) : GoodsItem(null, false) {

        private var goodsItemBinding: LayoutShopGoodsItemBinding? = null

        override fun getItemLayoutRes() = R.layout.layout_shop_goods_item

        override fun onViewAttachedToWindow(holder: HiViewHolder) {
            val viewGroup = holder.itemView.parent as ViewGroup
            val availableWidth =
                rvShopGoods.measuredWidth - rvShopGoods.paddingStart - rvShopGoods.paddingEnd

            if (goodsItemBinding == null) {
                goodsItemBinding = holder.bindingView()
            }
            goodsItemBinding?.run {
                val itemWidth = availableWidth / 3
                val layoutParams = ivShopGoodsIcon.layoutParams
                layoutParams.width = itemWidth
                layoutParams.height = itemWidth
                ivShopGoodsIcon.layoutParams = layoutParams
            }

        }

        override fun onBindData(holder: HiViewHolder, position: Int) {
            super.onBindData(holder, position)
            if (goodsItemBinding == null) {
                goodsItemBinding = holder.bindingView()
            }
            goodsItemBinding?.run {
                val goodsDetail = shopGoodsList[position]
                ivShopGoodsIcon.loadUrl(goodsDetail.goods_image_url)
                tvShopGoodsName.text = goodsDetail.goods_name
                tvShopGoodsPrice.text = String.format("￥%s", goodsDetail.min_group_price)
                tvShopGoodsSales.text = String.format("销售量：%s件", goodsDetail.sales_tip)


            }
        }
    }
}