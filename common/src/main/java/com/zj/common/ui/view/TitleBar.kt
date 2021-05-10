package com.zj.common.ui.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.RelativeLayout
import android.widget.TextView
import com.zj.common.R
import com.zj.hi_ui.ui.icfont.IconFontTextView

class TitleBar @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {

    private var toolbarActionRight: TextView
    private var toolbarTitle: TextView
    private var toolbarActionBack: IconFontTextView

    init {
        LayoutInflater.from(context).inflate(R.layout.layout_title_bar, this, true)
        toolbarActionBack = findViewById(R.id.toolbar_action_back)
        toolbarTitle = findViewById(R.id.toolbar_title)
        toolbarActionRight = findViewById(R.id.toolbar_action_right)

        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.TitleBar)
        val titleBackIcon = typedArray.getString(R.styleable.TitleBar_titleBackIcon)
        val title = typedArray.getString(R.styleable.TitleBar_toolbarTitle)
        val titleActionRight = typedArray.getString(R.styleable.TitleBar_titleActionRight)
        val textColor = typedArray.getColor(R.styleable.TitleBar_textColor, 0)

        val backVisibility = typedArray.getBoolean(R.styleable.TitleBar_backVisibility, true)
        val rightVisibility = typedArray.getBoolean(R.styleable.TitleBar_rightVisibility, true)
        typedArray.recycle()

        if (textColor != 0) {
            toolbarActionBack.setTextColor(textColor)
            toolbarTitle.setTextColor(textColor)
            toolbarTitle.setTextColor(textColor)
        }

        if (!titleBackIcon.isNullOrEmpty()) {
            toolbarActionBack.text = titleBackIcon
        }

        if (!title.isNullOrEmpty()) {
            toolbarTitle.text = title
        }

        if (!titleActionRight.isNullOrEmpty()) {
            toolbarActionRight.text = titleActionRight
        }

        toolbarActionBack.visibility = if (backVisibility) VISIBLE else GONE
        toolbarActionRight.visibility = if (rightVisibility) VISIBLE else GONE
    }

    fun setBackClick(clickListener: OnClickListener) {
        toolbarActionBack.setOnClickListener(clickListener)
    }

    fun rightClick(clickListener: OnClickListener) {
        toolbarActionRight.setOnClickListener(clickListener)
    }

    fun toolbarTitle(title: String) {
        toolbarTitle.text = title
    }
}