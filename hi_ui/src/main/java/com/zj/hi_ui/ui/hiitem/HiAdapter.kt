package com.zj.hi_ui.ui.hiitem

import android.content.Context
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.lang.RuntimeException
import java.lang.reflect.ParameterizedType
import java.util.ArrayList

class HiAdapter(context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var mInflater = LayoutInflater.from(context)
    private var dataSets = ArrayList<HiDataItem<*, RecyclerView.ViewHolder>>()
    private var typeArrays = SparseArray<HiDataItem<*, RecyclerView.ViewHolder>>()

    fun addItem(index: Int, item: HiDataItem<*, RecyclerView.ViewHolder>, notify: Boolean) {
        if (index > 0) {
            dataSets.add(index, item)
        } else {
            dataSets.add(item)
        }

        val notifyPos = if (index > 0) index else dataSets.size

        if (notify) {
            notifyItemInserted(notifyPos)
        }
    }

    fun addItems(items: List<HiDataItem<*, RecyclerView.ViewHolder>>, notify: Boolean) {
        val start = dataSets.size
        items.forEach {
            dataSets.add(it)
        }
        notifyItemRangeInserted(start, items.size)
    }

    fun refreshItem(dataItem: HiDataItem<*, out RecyclerView.ViewHolder>) {
        val indexOf = dataSets.indexOf(dataItem)
        notifyItemChanged(indexOf)
    }

    /**
     * 从指定位置移除item
     */
    fun removeItemAt(index: Int): HiDataItem<*, RecyclerView.ViewHolder>? {
        return if (index > 0 && index < dataSets.size) {
            val removeAt = dataSets.removeAt(index)
            notifyItemRemoved(index)
            removeAt
        } else {
            null
        }
    }

    /**
     * 移除指定item
     */
    fun removeItem(dataItem: HiDataItem<*, out RecyclerView.ViewHolder>) {
        val index: Int = dataSets.indexOf(dataItem)
        removeItemAt(index)
    }

    override fun getItemViewType(position: Int): Int {
        val dataItem = dataSets[position]
        val type = dataItem.javaClass.hashCode()

        // 如果还没有添加item类型则添加
        if (typeArrays.indexOfKey(type) < 0) {
            typeArrays.put(type, dataItem)
        }
        return type
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val dataItem = typeArrays.get(viewType)
        var itemView = dataItem.getItemView(parent)
        if (itemView == null) {
            val itemLayoutRes = dataItem.getItemLayoutRes()
            if (itemLayoutRes < 0)
                throw RuntimeException("dataItem" + dataItem.javaClass.name + " must override getItemView or getItemLayoutRes")
            itemView = mInflater.inflate(itemLayoutRes, parent, false)
        }
        return createViewHolderInternal(dataItem.javaClass, itemView!!)
    }

    private fun createViewHolderInternal(
        javaClass: Class<HiDataItem<*, RecyclerView.ViewHolder>>,
        itemView: View
    ): RecyclerView.ViewHolder {
        //得到该item的夫类类型，HiDataItem.class  class 也是type的一个子类。
        //type的子类常见的有 class，类泛型,ParameterizedType参数泛型 ，TypeVariable字段泛型
        //所以进一步判断它是不是参数泛型
        val superclass = javaClass.genericSuperclass
        if (superclass is ParameterizedType) {
            //得到它携带的泛型参数的数组
            val arguments = superclass.actualTypeArguments
            //挨个遍历判断
            for (argument in arguments) {
                //RecyclerView.ViewHolder 子类 类型的。
                if (argument is Class<*> &&
                    RecyclerView.ViewHolder::class.java.isAssignableFrom(argument)
                ) {
                    try {
                        return argument.getConstructor(View::class.java)
                            .newInstance(itemView) as RecyclerView.ViewHolder
                    } catch (e: Throwable) {
                        e.printStackTrace()
                    }
                }
            }
        }
        return object : RecyclerView.ViewHolder(itemView) {}
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val itemViewType = getItemViewType(position)
        typeArrays[itemViewType]?.onBindData(holder, position)
    }

    /**
     * 为列表上的item适配网格布局
     */
    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        val layoutManager = recyclerView.layoutManager
        if (layoutManager is GridLayoutManager) {
            val spanCount = layoutManager.spanCount
            layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    val itemViewType = getItemViewType(position)
                    val spanSize = typeArrays[itemViewType]?.getSpanSize() ?: 0
                    return if (spanSize <= 0) spanCount else spanSize
                }

            }
        }
    }

    override fun getItemCount() = dataSets.size
}