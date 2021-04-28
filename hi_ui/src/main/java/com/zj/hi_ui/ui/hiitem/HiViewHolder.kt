package com.zj.hi_ui.ui.hiitem

import android.util.SparseArray
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.zj.hi_ui.ui.banner.HiBanner

open class HiViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

//    var binding: T? = DataBindingUtil.bind<T>(view)

    private var viewCache = SparseArray<View>()

    fun <T : View> findViewById(viewId: Int): T? {
        var view = viewCache.get(viewId)
        if (view == null) {
            view = itemView.findViewById<T>(viewId)
            viewCache.put(viewId, view)
        }
        return view as? T
    }

    fun <Binding : ViewDataBinding> bindingView(): Binding? {
        return DataBindingUtil.bind<Binding>(view)
    }
}