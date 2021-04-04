package com.zj.hi_ui.ui.banner.indicator

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.annotation.DrawableRes
import com.zj.hi_library.util.HiDisplayUtil
import com.zj.hi_ui.R

class HiCircleIndicator @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), HiIndicator<FrameLayout?> {

    companion object {
        private const val VWC = ViewGroup.LayoutParams.WRAP_CONTENT
    }

    /**
     * 普通状态下的指示点
     */
    @DrawableRes
    private val shapeNormal = R.drawable.shape_point_normal

    /**
     * 选中状态下的指示点
     */
    @DrawableRes
    private val shapeSelect = R.drawable.shape_point_select

    /**
     * 指示点左右内间距
     */
    private var mPointLeftRightPadding = 0

    /**
     * 指示点上下内间距
     */
    private var mPointTopBottomPadding = 0

    init {
        mPointLeftRightPadding = HiDisplayUtil.dp2px(5f, context.resources)
        mPointTopBottomPadding = HiDisplayUtil.dp2px(15f, context.resources)
    }

    override fun get(): FrameLayout = this

    override fun onInflate(count: Int) {
        if (count <= 0) {
            return
        }
        removeAllViews()
        val indicatorGroup = LinearLayout(context)
        indicatorGroup.orientation = LinearLayout.HORIZONTAL

        val imageViewParams = LinearLayout.LayoutParams(VWC, VWC)
        imageViewParams.gravity = Gravity.CENTER_VERTICAL
        imageViewParams.setMargins(
            mPointLeftRightPadding,
            mPointTopBottomPadding,
            mPointLeftRightPadding,
            mPointTopBottomPadding
        )
        var imageView: ImageView
        //until [0,3) 0、1、2
        //.. rangeTo [0,3] 0、1、2、3
        //downTo [3,0] 3、2、1、0
        for (i in 0 until count) {
            imageView = ImageView(context)
            imageView.layoutParams = imageViewParams
            if (i == 0) {
                imageView.setImageResource(shapeSelect)
            } else {
                imageView.setImageResource(shapeNormal)
            }
            indicatorGroup.addView(imageView, imageViewParams)
        }
        val groupViewParams = LayoutParams(VWC, VWC)
        groupViewParams.gravity = Gravity.CENTER or Gravity.BOTTOM
        addView(indicatorGroup, groupViewParams)
    }

    override fun onPointChange(current: Int, count: Int) {
        val viewGroup = getChildAt(0) as ViewGroup
        for (i in 0 until viewGroup.childCount) {
            val imageView = viewGroup.getChildAt(i) as ImageView
            if (i == current) {
                imageView.setImageResource(shapeSelect)
            } else {
                imageView.setImageResource(shapeNormal)
            }
            imageView.requestLayout()
        }
    }
}