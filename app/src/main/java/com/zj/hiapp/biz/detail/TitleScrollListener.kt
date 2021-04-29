package com.zj.hiapp.biz.detail

import android.graphics.Color
import androidx.recyclerview.widget.RecyclerView
import com.zj.hi_library.util.ColorUtil
import kotlin.math.abs
import kotlin.math.min

class TitleScrollListener(private val thresholdsDp: Float = 100f, val callback: (Int) -> Unit) :
    RecyclerView.OnScrollListener() {


    private var lastFraction: Float = 0f

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        // 计算新的颜色值，从透明到白色渐变
        val viewHolder = recyclerView.findViewHolderForAdapterPosition(0) ?: return
        val top = abs(viewHolder.itemView.top)
        val fraction = top / thresholdsDp
        if (lastFraction > 1f) {
            lastFraction = fraction
            return
        }
        val newColor = ColorUtil.getCurrentColor(Color.TRANSPARENT, Color.WHITE, min(fraction, 1f))
        callback.invoke(newColor)
        lastFraction = fraction
    }
}