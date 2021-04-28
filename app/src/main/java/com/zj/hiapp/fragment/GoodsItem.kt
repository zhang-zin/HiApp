package com.zj.hiapp.fragment

import android.content.Context
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.zj.hi_library.util.HiDisplayUtil
import com.zj.hi_ui.ui.hiitem.HiDataItem
import com.zj.hi_ui.ui.hiitem.HiViewHolder
import com.zj.hiapp.R

open class GoodsItem(data: String, val hotTab: Boolean) :
    HiDataItem<String, HiViewHolder>(data) {
    val MAX_TAG_SIZE = 3
    override fun onBindData(holder: HiViewHolder, position: Int) {

        val context = holder.itemView.context

       /* val itemLabelContainer = holder.item_label_container
        if (!TextUtils.isEmpty(goodsModel.tags)) {
            itemLabelContainer.visibility = View.VISIBLE
            val split = goodsModel.tags.split(" ")
            for (index in split.indices) { //0...split.size-1
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
                labelView.text = split[index]
            }
        } else {
            itemLabelContainer.visibility = View.GONE
        }*/

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