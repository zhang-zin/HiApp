package com.zj.hiapp.fragment

import android.content.Context
import android.text.TextUtils
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.zj.common.ui.view.loadUrl
import com.zj.hi_library.util.HiDisplayUtil
import com.zj.hi_ui.ui.hiitem.HiDataItem
import com.zj.hi_ui.ui.hiitem.HiViewHolder
import com.zj.hiapp.R
import com.zj.hiapp.http.model.GoodsDetail

open class GoodsItem(private val data: GoodsDetail?, val hotTab: Boolean) :
    HiDataItem<GoodsDetail, HiViewHolder>(data) {
    val MAX_TAG_SIZE = 3
    override fun onBindData(holder: HiViewHolder, position: Int) {

        val context = holder.itemView.context

        val itemLabelContainer = holder.findViewById<LinearLayout>(R.id.item_label_container)
        if (itemLabelContainer != null) {
            if (data == null) return
            if (data.unified_tags.isNotEmpty()) {
                itemLabelContainer.visibility = View.VISIBLE
                for (index in data.unified_tags.indices) {
                    if (index > 2)
                        return
                    //0  ---3
                    val childCount = itemLabelContainer.childCount
                    if (index > MAX_TAG_SIZE - 1) {
                        //倒叙
                        for (index in childCount - 1 downTo MAX_TAG_SIZE - 1) {
                            // itemLabelContainer childcount =5
                            // 3，后面的两个都需要被删除
                            itemLabelContainer.removeViewAt(index)
                        }
                        break
                    }
                    //这里有个问题，有着一个服用的问题   5 ,4
                    //解决上下滑动复用的问题--重复创建的问题
                    val labelView: TextView = if (index > childCount - 1) {
                        val view = createLabelView(context, index != 0)
                        itemLabelContainer.addView(view)
                        view
                    } else {
                        itemLabelContainer.getChildAt(index) as TextView
                    }
                    labelView.text = data.unified_tags[index]
                }
            } else {
                itemLabelContainer.visibility = View.GONE
            }

            val itemImage = holder.findViewById<ImageView>(R.id.item_image)
            val itemTitle = holder.findViewById<TextView>(R.id.item_title)
            val itemPrice = holder.findViewById<TextView>(R.id.item_price)
            val itemSaleDesc = holder.findViewById<TextView>(R.id.item_sale_desc)

            itemImage?.loadUrl(data.goods_image_url)
            itemTitle?.text = data.goods_name
            itemPrice?.text = String.format("￥%s", data.min_group_price)
            itemSaleDesc?.text = String.format("销售量：%s件", data.sales_tip)
        }

        if (!hotTab) {
            val margin = HiDisplayUtil.dp2px(2f)
            val params = holder.itemView.layoutParams as RecyclerView.LayoutParams
            val parentLeft = hiAdapter?.getAttachRecyclerView()?.left ?: 0
            val parentPaddingLeft = hiAdapter?.getAttachRecyclerView()?.paddingLeft ?: 0
            val itemLeft = holder.itemView.left
            if (itemLeft == (parentLeft + parentPaddingLeft)) {
                params.rightMargin = margin
            } else {
                params.leftMargin = margin
            }
            holder.itemView.layoutParams = params
        }
    }

    private fun createLabelView(context: Context, withLeftMargin: Boolean): TextView {
        val labelView = TextView(context)
        labelView.setTextColor(ContextCompat.getColor(context, R.color.color_e75))
        labelView.setBackgroundResource(R.drawable.shape_goods_label)
        labelView.textSize = 11f
        labelView.gravity = Gravity.CENTER
        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            HiDisplayUtil.dp2px(16f)
        )
        params.leftMargin = if (withLeftMargin) HiDisplayUtil.dp2px(5f) else 0
        labelView.layoutParams = params
        return labelView
    }

    override fun getItemLayoutRes(): Int {
        return if (hotTab) R.layout.layout_home_goods_list_item1 else R.layout.layout_home_goods_list_item2
    }

    override fun getSpanSize(): Int {
        return if (hotTab) super.getSpanSize() else 1
    }
}