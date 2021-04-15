package com.zj.hi_ui.ui.slide

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.zj.hi_ui.R
import com.zj.hi_ui.ui.hiitem.HiViewHolder
import kotlinx.android.synthetic.main.hi_slider_menu_item.view.*

class HiSliderView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private var menuItemAttrs: MenuItemAttrs
    private val MENU_ITEM_WIDTH = applyUnit(140f).toInt()
    private val MENU_ITEM_HEIGHT = applyUnit(45f).toInt()
    private val MENU_ITEM_TEXT_SIZE = applyUnit(16f, TypedValue.COMPLEX_UNIT_SP).toInt()

    private val TEXT_COLOR_NORMAL = Color.parseColor("#666666")
    private val TEXT_COLOR_SELECT = Color.parseColor("#4282f4")

    private val BG_COLOR_NORMAL = Color.parseColor("#F7F8F9")
    private val BG_COLOR_SELECT = Color.parseColor("#ffffff")

    private val menuView = RecyclerView(context)
    private val contentView = RecyclerView(context)

    private val MENU_ITEM_LAYOUT_RES_ID = R.layout.hi_slider_menu_item
    private val CONTENT_LAYOUT_RES_ID = R.layout.hi_slider_content_item

    init {
        orientation = HORIZONTAL
        menuItemAttrs = parseSlideAttrs(attrs)

        menuView.layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT)
        menuView.overScrollMode = View.OVER_SCROLL_NEVER
        menuView.itemAnimator = null

        contentView.layoutParams =
            LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        contentView.overScrollMode = View.OVER_SCROLL_NEVER
        contentView.itemAnimator = null

        addView(menuView)
        addView(contentView)
    }

    /**
     * 解析侧边大分类的属性
     */
    private fun parseSlideAttrs(attributeSet: AttributeSet?): MenuItemAttrs {

        val attrs = context.obtainStyledAttributes(attributeSet, R.styleable.HiSlideView)

        val itemWidth =
            attrs.getDimensionPixelSize(R.styleable.HiSlideView_menuItemWidth, MENU_ITEM_WIDTH)
        val itemHeight =
            attrs.getDimensionPixelSize(R.styleable.HiSlideView_menuItemHeight, MENU_ITEM_HEIGHT)
        val itemTextSize = attrs.getDimensionPixelSize(
            R.styleable.HiSlideView_menuItemTextSize,
            MENU_ITEM_TEXT_SIZE
        )
        val itemSelectTextSize = attrs.getDimensionPixelSize(
            R.styleable.HiSlideView_menuItemSelectTextSize,
            MENU_ITEM_TEXT_SIZE
        )

        val menuItemTextColor = attrs.getColorStateList(R.styleable.HiSlideView_menuItemTextColor)
            ?: generateColorStateList()

        val menuItemIndicator = attrs.getDrawable(R.styleable.HiSlideView_menuItemIndicator)
            ?: ContextCompat.getDrawable(context, R.drawable.shape_slide_item_default_indicator)

        val menuItemBackgroundColor =
            attrs.getColor(R.styleable.HiSlideView_menuItemBackgroundColor, BG_COLOR_NORMAL)
        val menuItemBackgroundSelectColor = attrs.getColor(
            R.styleable.HiSlideView_menuItemSelectBackgroundColor,
            BG_COLOR_SELECT
        )

        attrs.recycle()
        return MenuItemAttrs(
            itemWidth,
            itemHeight,
            itemTextSize,
            itemSelectTextSize,
            menuItemTextColor,
            menuItemIndicator,
            menuItemBackgroundColor,
            menuItemBackgroundSelectColor
        )
    }

    data class MenuItemAttrs(
        val width: Int,
        val height: Int,
        val textSize: Int,
        val selectTextSize: Int,
        val textColor: ColorStateList,
        val indicator: Drawable?,
        val backgroundColor: Int,
        val backgroundSelectColor: Int
    )

    private fun generateColorStateList(): ColorStateList {
        val states = Array(2) { IntArray(2) }
        val colors = IntArray(2)

        colors[0] = TEXT_COLOR_SELECT
        colors[1] = TEXT_COLOR_NORMAL

        states[0] = IntArray(1) { android.R.attr.state_selected }
        states[1] = IntArray(1)
        return ColorStateList(states, colors)
    }

    /**
     * 默认 dp ---> px
     * 可以将unit格式 ---> px
     */
    private fun applyUnit(value: Float, unit: Int = TypedValue.COMPLEX_UNIT_DIP): Float {
        return TypedValue.applyDimension(unit, value, context.resources.displayMetrics)
    }

    //region 绑定内容
    fun bindContentView(
        layoutRes: Int = CONTENT_LAYOUT_RES_ID,
        itemCount: Int,
        itemDecoration: RecyclerView.ItemDecoration?,
        layoutManager: RecyclerView.LayoutManager,
        onBindView: (HiViewHolder, Int) -> Unit,
        onItemClick: (HiViewHolder, Int) -> Unit
    ) {
        if (contentView.layoutManager == null) {
            contentView.layoutManager = layoutManager
            contentView.adapter = ContentAdapter(layoutRes)
            itemDecoration?.run {
                contentView.addItemDecoration(this)
            }
        }

        val contentAdapter = contentView.adapter as ContentAdapter
        contentAdapter.notify(itemCount, onBindView, onItemClick)
        contentAdapter.notifyDataSetChanged()

        contentView.scrollToPosition(0)
    }

    inner class ContentAdapter(private val layoutRes: Int) : RecyclerView.Adapter<HiViewHolder>() {

        private var count = 0
        private lateinit var onBindView: (HiViewHolder, Int) -> Unit
        private lateinit var onItemClick: (HiViewHolder, Int) -> Unit

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HiViewHolder {
            val itemView = LayoutInflater.from(context).inflate(layoutRes, parent, false)

            val remainSpace = width - paddingStart - paddingEnd - menuItemAttrs.width
            val layoutManager = (parent as RecyclerView).layoutManager
            var spanCount = 0

            if (layoutManager is GridLayoutManager) {
                spanCount = layoutManager.spanCount
            } else if (layoutManager is StaggeredGridLayoutManager) {
                spanCount = layoutManager.spanCount
            }

            if (spanCount > 0) {
                val itemWidth = remainSpace / spanCount
                itemView.layoutParams = RecyclerView.LayoutParams(itemWidth, itemWidth)
            }

            return HiViewHolder(itemView)
        }

        override fun onBindViewHolder(holder: HiViewHolder, position: Int) {
            onBindView(holder, position)
            holder.itemView.setOnClickListener {
                onItemClick(holder, position)
            }
        }

        override fun getItemCount(): Int {
            return count
        }

        fun notify(
            itemCount: Int,
            onBindView: (HiViewHolder, Int) -> Unit,
            onItemClick: (HiViewHolder, Int) -> Unit
        ) {
            count = itemCount
            this.onBindView = onBindView
            this.onItemClick = onItemClick
        }
    }
    //endregion

    //region 绑定menu布局
    fun bindMenuView(
        layoutRes: Int = MENU_ITEM_LAYOUT_RES_ID,
        itemCount: Int,
        onBindView: (HiViewHolder, Int) -> Unit,
        onItemClick: (HiViewHolder, Int) -> Unit
    ) {
        menuView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        menuView.adapter = MenuAdapter(layoutRes, itemCount, onBindView, onItemClick)
    }

    //region MenuAdapter
    inner class MenuAdapter(
        private val layoutRes: Int,
        private val count: Int,
        private val onBindView: (HiViewHolder, Int) -> Unit,
        private val onItemClick: (HiViewHolder, Int) -> Unit
    ) : RecyclerView.Adapter<HiViewHolder>() {

        private var currentSelectIndex = 0
        private var lastSelectIndex = 0

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HiViewHolder {
            val itemView = LayoutInflater.from(context).inflate(layoutRes, parent, false)
            val params = RecyclerView.LayoutParams(menuItemAttrs.width, menuItemAttrs.height)
            itemView.layoutParams = params
            itemView.setBackgroundColor(menuItemAttrs.backgroundColor)

            itemView.findViewById<TextView>(R.id.menu_item_title)
                ?.setTextColor(menuItemAttrs.textColor)

            itemView.findViewById<ImageView>(R.id.menu_item_indicator)
                ?.setImageDrawable(menuItemAttrs.indicator)

            return HiViewHolder(itemView)
        }

        override fun onBindViewHolder(holder: HiViewHolder, position: Int) {
            holder.view.setOnClickListener {
                currentSelectIndex = position
                notifyItemChanged(currentSelectIndex)
                notifyItemChanged(lastSelectIndex)
            }
            if (currentSelectIndex == position) {
                onItemClick(holder, position)
                lastSelectIndex = currentSelectIndex
            }
            applyItemAttr(position, holder)
            onBindView(holder, position)

        }

        private fun applyItemAttr(position: Int, holder: HiViewHolder) {
            val selected = position == currentSelectIndex

            val titleView: TextView? = holder.itemView.menu_item_title
            val indicatorView: ImageView? = holder.itemView.menu_item_indicator

            indicatorView?.visibility = if (selected) VISIBLE else GONE
            titleView?.setTextSize(
                TypedValue.COMPLEX_UNIT_PX,
                (if (selected) menuItemAttrs.selectTextSize else menuItemAttrs.textSize).toFloat()
            )
            holder.itemView.setBackgroundColor(if (selected) menuItemAttrs.backgroundSelectColor else menuItemAttrs.backgroundColor)
            titleView?.isSelected = selected
        }

        override fun getItemCount() = count
    }
    //endregion

    //endregion
}