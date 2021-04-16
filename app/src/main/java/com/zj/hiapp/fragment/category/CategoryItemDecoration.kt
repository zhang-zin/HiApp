package com.zj.hiapp.fragment.category

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Paint.ANTI_ALIAS_FLAG
import android.graphics.Rect
import android.text.TextUtils
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.zj.hi_library.util.HiDisplayUtil

class CategoryItemDecoration(val callback: (Int) -> String, private val spanCount: Int) :
    RecyclerView.ItemDecoration() {

    private val groupFirstPositions = mutableMapOf<String, Int>()
    private val paint = Paint(ANTI_ALIAS_FLAG)

    init {
        paint.style = Paint.Style.FILL
        paint.color = Color.BLACK
        paint.isFakeBoldText = false
        paint.textSize = HiDisplayUtil.dp2px(16f).toFloat()
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val currentViewIndex = parent.getChildAdapterPosition(view)
        if (currentViewIndex >= parent.adapter!!.itemCount || currentViewIndex < 0) return

        val currentGroupName = callback(currentViewIndex)
        val lastGroupName = if (currentViewIndex > 0) callback(currentViewIndex - 1) else ""

        val isSame = TextUtils.equals(currentGroupName, lastGroupName)
        if (!groupFirstPositions.containsKey(currentGroupName)) {
            groupFirstPositions[currentGroupName] = currentViewIndex
        }

        val firstRowPosition = groupFirstPositions[currentGroupName] ?: 0 //每组第一个元素在列中的位置
        val samRow = currentViewIndex - firstRowPosition in 0 until spanCount

        if (!isSame || samRow) {
            outRect.set(0, HiDisplayUtil.dp2px(40f), 0, 0)
            return
        }

        super.getItemOffsets(outRect, view, parent, state)
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val childCount = parent.childCount
        for (index in 0 until childCount) {
            val childAt = parent.getChildAt(index)
            val childAdapterPosition = parent.getChildAdapterPosition(childAt)
            if (childAdapterPosition >= parent.adapter!!.itemCount || childAdapterPosition < 0) continue

            val currentGroupName = callback(childAdapterPosition)
            val groupFirstPosition = groupFirstPositions[currentGroupName]
            if (groupFirstPosition == childAdapterPosition) {
                val decorationBounds = Rect()
                //为了拿到当前item 的 左上右下的坐标信息 包含了，margin 和 扩展空间的
                parent.getDecoratedBoundsWithMargins(childAt, decorationBounds)

                val textBounds = Rect()
                paint.getTextBounds(currentGroupName, 0, currentGroupName.length, textBounds)

                c.drawText(
                    currentGroupName,
                    HiDisplayUtil.dp2px(16f).toFloat(),
                    (decorationBounds.top + 2 * textBounds.height()).toFloat(),
                    paint
                )
            }
        }
    }

    fun clear() {
        groupFirstPositions.clear()
    }
}