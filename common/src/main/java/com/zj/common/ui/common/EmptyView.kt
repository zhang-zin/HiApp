package com.zj.common.ui.common

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import com.zj.common.R

class EmptyView : LinearLayout {

    private var emptyIcon: TextView
    private var emptyTitle: TextView
    private var button: Button

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attributes: AttributeSet?) : this(context, attributes, 0)

    constructor(context: Context, attributes: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attributes,
        defStyleAttr
    ) {
        orientation = VERTICAL
        gravity = Gravity.CENTER
        LayoutInflater.from(context).inflate(R.layout.layout_empty_view, this, true)

        emptyIcon = findViewById(R.id.empty_icon)
        emptyTitle = findViewById(R.id.empty_title)
        button = findViewById(R.id.empty_action)

        val typeface = Typeface.createFromAsset(context.assets, "fonts/iconfont.ttf")
        emptyIcon.typeface = typeface

        val typedArray: TypedArray =
            context.obtainStyledAttributes(attributes, R.styleable.EmptyView)

        val icon = typedArray.getResourceId(R.styleable.EmptyView_emptyIcon, -1)
        val title = typedArray.getString(R.styleable.EmptyView_emptyTitle)
        val action = typedArray.getString(R.styleable.EmptyView_emptyAction)
        typedArray.recycle()

        if (icon == -1) {
            emptyIcon.visibility = GONE
        } else {
            emptyIcon.visibility = VISIBLE
            emptyIcon.setText(R.string.list_empty)
        }

        if (title.isNullOrEmpty()) {
            emptyTitle.visibility = GONE
        } else {
            emptyTitle.visibility = VISIBLE
            emptyTitle.text = title
        }

        if (action.isNullOrEmpty()) {
            button.visibility = GONE
        } else {
            button.visibility = VISIBLE
            button.text = action
        }
    }

    fun setActionClick(clickListener: OnClickListener) {
        button.setOnClickListener { clickListener }
    }
}