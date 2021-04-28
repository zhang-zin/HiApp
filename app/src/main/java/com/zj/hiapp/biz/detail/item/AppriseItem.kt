package com.zj.hiapp.biz.detail.item

import android.content.res.ColorStateList
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.google.android.material.chip.Chip
import com.zj.common.ui.view.loadCircle
import com.zj.hi_library.util.HiDisplayUtil
import com.zj.hi_ui.ui.hiitem.HiDataItem
import com.zj.hi_ui.ui.hiitem.HiViewHolder
import com.zj.hiapp.R
import com.zj.hiapp.databinding.LayoutDetailItemAppraiseBinding
import com.zj.hiapp.http.model.GoodDetailModel

class AppriseItem(
    private val quantitySold: Int,
    private val tagString: Array<String>,
    private val appriseArray: Array<String>
) :
    HiDataItem<GoodDetailModel, HiViewHolder>() {

    var binding: LayoutDetailItemAppraiseBinding? = null

    override fun onBindData(holder: HiViewHolder, position: Int) {
        if (binding == null) {
            binding = holder.bindingView()
        }

        binding?.run {
            tvGoodsAppraise.text = String.format("商品评价(%s)", quantitySold)
            val context = root.context
            for (index in tagString.indices) {
                val chipLabel = if (index < appraiseTag.childCount) {
                    appraiseTag.getChildAt(index) as Chip
                } else {
                    val chip = Chip(context)
                    chip.chipCornerRadius = HiDisplayUtil.dp2px(4f).toFloat()
                    chip.chipBackgroundColor = ColorStateList.valueOf(
                        ContextCompat.getColor(
                            context,
                            R.color.color_faf0
                        )
                    )
                    chip.setTextColor(ContextCompat.getColor(context, R.color.color_999))
                    chip.textSize = 14f
                    chip.gravity = Gravity.CENTER
                    chip.isCheckedIconVisible = false
                    chip.isCheckable = false
                    chip.isChipIconVisible = false

                    appraiseTag.addView(chip)
                    chip
                }
                chipLabel.text = tagString[index]
            }
            for (index in appriseArray.indices) {
                val item = if (index < llAppraiseItem.childCount) {
                    llAppraiseItem.getChildAt(index)
                } else {
                    val view = LayoutInflater.from(context)
                        .inflate(R.layout.layout_appraise_item, llAppraiseItem, false)
                    llAppraiseItem.addView(view)
                    view
                }
                val userIcon = item.findViewById<ImageView>(R.id.iv_user_icon)
                val userName = item.findViewById<TextView>(R.id.tv_user_name)
                val appriseDesc = item.findViewById<TextView>(R.id.tv_apprise_desc)
                userIcon.loadCircle("https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fpic1.zhimg.com%2F50%2Fv2-e73ebe5fb7fbae39d69ed94dcc82f145_hd.jpg&refer=http%3A%2F%2Fpic1.zhimg.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1622034301&t=dd0acc5a8a5f929469069e8ccfba3dd7")
                userName.text = "小王子$index"
                appriseDesc.text = appriseArray [index]
            }
        }

    }

    override fun getItemLayoutRes() = R.layout.layout_detail_item_appraise
}