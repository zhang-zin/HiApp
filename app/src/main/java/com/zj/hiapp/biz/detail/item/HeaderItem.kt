package com.zj.hiapp.biz.detail.item

import android.text.SpannableString
import android.text.Spanned
import android.text.style.AbsoluteSizeSpan
import android.widget.ImageView
import com.zj.common.ui.view.loadUrl
import com.zj.hi_ui.ui.banner.core.HiBannerMo
import com.zj.hi_ui.ui.banner.indicator.HiNumIndicator
import com.zj.hi_ui.ui.hiitem.HiDataItem
import com.zj.hi_ui.ui.hiitem.HiViewHolder
import com.zj.hiapp.R
import com.zj.hiapp.databinding.LayoutDetailItemHeaderBinding
import com.zj.hiapp.http.model.GoodDetailModel

class HeaderItem(
    private val goodsGalleryUrls: List<String>,
    private val goodsPrice: String,
    private val salesTip: String,
    private val goodsName: String
) : HiDataItem<GoodDetailModel, HiViewHolder>() {

    override fun onBindData(holder: HiViewHolder, position: Int) {
        val bindingView = holder.bindingView<LayoutDetailItemHeaderBinding>()
        val bannerItems = arrayListOf<HiBannerMo>()
        goodsGalleryUrls.forEach {
            val bannerMo = object : HiBannerMo() {}
            bannerMo.url = it
            bannerItems.add(bannerMo)
        }
        val context = holder.itemView.context
        bindingView?.run {
        hiBanner.setHiIndicator(HiNumIndicator(context))
        hiBanner.setBannerData(bannerItems)
        hiBanner.setBindAdapter { viewHolder, mo, _ ->
            val imageView = viewHolder?.rootView as ImageView
            imageView.loadUrl(mo?.url)
        }
        price.text = spanPrice("¥$goodsPrice")
        goodsTitle.text = goodsName
        saleDesc.text = String.format("已拼%s件", salesTip)
        }
    }

    private fun spanPrice(price: String?): CharSequence {
        if (price.isNullOrEmpty()) return ""
        val ss = SpannableString(price)
        ss.setSpan(AbsoluteSizeSpan(18, true), 1, ss.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        return ss
    }

    override fun getItemLayoutRes() = R.layout.layout_detail_item_header

}