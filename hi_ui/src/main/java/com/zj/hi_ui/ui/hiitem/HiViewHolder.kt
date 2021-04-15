package com.zj.hi_ui.ui.hiitem

import android.util.SparseArray
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class HiViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

    init {
    }

    private var viewCache = SparseArray<View>()

    fun <T : View> findViewById(viewId: Int): T? {
        var view = viewCache.get(viewId)
        if (view == null) {
            view = itemView.findViewById<T>(viewId)
            viewCache.put(viewId, view)
        }
        return view as? T
    }
}