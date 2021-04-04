package com.zj.hi_ui.ui.hiitem

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

abstract class HiDataItem<DATA, VH : RecyclerView.ViewHolder>(data: DATA) {

    var mDate: DATA? = null
    var hiAdapter: HiAdapter? = null

    init {
        this.mDate = data
    }

    abstract fun onBindData(holder: RecyclerView.ViewHolder, position: Int)

    /**
     * 返回该item的布局资源id
     */
    open fun getItemLayoutRes(): Int {
        return -1;
    }

    fun setAdapter(adapter: HiAdapter) {
        this.hiAdapter = adapter
    }

    /**
     * 返回该item的视图view
     */
    open fun getItemView(parent: ViewGroup): View? {
        return null
    }

    /**
     * 刷新列表
     */
    fun refreshItem() {
        hiAdapter?.run {
           refreshItem(this@HiDataItem)
        }
    }

    /**
     * 从列表中移除
     */
    fun removeItem() {
        hiAdapter?.run {
            removeItem(this@HiDataItem)
        }
    }

    /**
     * 占几列
     */
    fun getSpanSize(): Int {
        return 0
    }
}