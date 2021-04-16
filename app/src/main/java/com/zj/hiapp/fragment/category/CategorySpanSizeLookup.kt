package com.zj.hiapp.fragment.category

import android.text.TextUtils
import android.util.SparseIntArray
import androidx.recyclerview.widget.GridLayoutManager
import com.zj.hiapp.http.model.ChildCategory

class CategorySpanSizeLookup(
    private val childrenList: MutableList<ChildCategory>,
    val spanCount: Int
) :
    GridLayoutManager.SpanSizeLookup() {

    private val groupSpanSizeOffset = SparseIntArray()

    override fun getSpanSize(position: Int): Int {
        var spanSize = 1
        val groupParentChapterId = childrenList[position].parentChapterId

        val nextGroupParentChapterId = if (position + 1 < childrenList.size) {
            childrenList[position + 1].parentChapterId
        } else
            -1

        if (groupParentChapterId == nextGroupParentChapterId) {
            spanSize = 1
        } else {
            //当前位置和 下一个位置 不再同一个分组
            //1 .要拿到当前组 position （所在组）在 groupSpanSizeOffset 的索引下标
            //2 .拿到 当前组前面一组 存储的 spansizeoffset 偏移量
            //3 .给当前组最后一个item 分配 spansize count
            val size = groupSpanSizeOffset.size()
            val indexOfKey = groupSpanSizeOffset.indexOfKey(position)
            val lastGroupOffset = if (size <= 0) {
                // 当前还没有存入偏移量
                0
            } else if (indexOfKey >= 0) {
                if (indexOfKey == 0) 0 else groupSpanSizeOffset.valueAt(indexOfKey - 1)
            } else {
                groupSpanSizeOffset.valueAt(size - 1)
            }

            spanSize = spanCount - (position + lastGroupOffset) % spanCount
            if (indexOfKey < 0) {
                val groupOffset = lastGroupOffset + spanSize - 1
                groupSpanSizeOffset.put(position, groupOffset)
            }
        }
        return spanSize
    }
}