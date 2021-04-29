package com.zj.hiapp.biz.detail.item

import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.zj.common.ui.view.loadUrl
import com.zj.hi_ui.ui.hiitem.HiDataItem
import com.zj.hi_ui.ui.hiitem.HiViewHolder

class GalleryItem(private val imageUrl: String) : HiDataItem<String, HiViewHolder>() {
    private var parentWidth: Int = 0

    override fun onBindData(holder: HiViewHolder, position: Int) {

        val imageView = holder.itemView as ImageView
        imageView.loadUrl(imageUrl) {
            val drawableWidth = it.intrinsicWidth
            val drawableHeight = it.intrinsicHeight
            val layoutParams = imageView.layoutParams ?: RecyclerView.LayoutParams(
                parentWidth,
                RecyclerView.LayoutParams.WRAP_CONTENT
            )
            layoutParams.width = parentWidth
            layoutParams.height = (drawableHeight / (drawableWidth * 1.0f / parentWidth)).toInt()
            imageView.layoutParams = layoutParams
            ViewCompat.setBackground(imageView, it)
        }
    }

    override fun getItemView(parent: ViewGroup): View {
        val imageView = ImageView(parent.context)
        imageView.scaleType = ImageView.ScaleType.CENTER_CROP
        imageView.setBackgroundColor(Color.WHITE)
        return imageView
    }


    override fun onViewAttachedToWindow(holder: HiViewHolder) {
        parentWidth = (holder.itemView.parent as ViewGroup).measuredWidth

        val params = holder.itemView.layoutParams
        if (params.width != parentWidth) {
            params.width = parentWidth
            params.height = parentWidth
            holder.itemView.layoutParams = params
        }
    }
}