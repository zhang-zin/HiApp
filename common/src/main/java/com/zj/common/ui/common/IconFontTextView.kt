package com.zj.common.ui.common

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

/**
 * 支持inconFont资源的引用，可以在布局文件直接设置text
 */
class IconFontTextView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet?,
    defStyleAttr: Int = 0
) : AppCompatTextView(context, attributeSet, defStyleAttr) {

    init {
        val typeface = Typeface.createFromAsset(context.assets, "/fonts/iconfont.ttf")
        setTypeface(typeface)
    }
}