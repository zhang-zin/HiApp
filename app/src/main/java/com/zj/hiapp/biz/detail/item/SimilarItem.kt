package com.zj.hiapp.biz.detail.item

import com.zj.hi_ui.ui.hiitem.HiDataItem
import com.zj.hi_ui.ui.hiitem.HiViewHolder
import com.zj.hiapp.R

class SimilarItem : HiDataItem<Any, HiViewHolder>() {
    override fun onBindData(holder: HiViewHolder, position: Int) {
    }

    override fun getItemLayoutRes(): Int {
        return R.layout.layout_detail_item_similar_title
    }
}