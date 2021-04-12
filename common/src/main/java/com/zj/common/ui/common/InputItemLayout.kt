package com.zj.common.ui.common

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.ColorDrawable
import android.text.InputFilter
import android.text.InputType
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import com.zj.common.R

open class InputItemLayout : LinearLayout {

    private var topLineStyle: Line
    private var bottomLineStyle: Line
    private val topPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val bottomPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        dividerDrawable = ColorDrawable()
        showDividers = SHOW_DIVIDER_BEGINNING
        orientation = HORIZONTAL
        val attributeSet = context.obtainStyledAttributes(attrs, R.styleable.InputItemLayout)

        //解析title资源属性
        val titleStyleId =
            attributeSet.getResourceId(R.styleable.InputItemLayout_titleTextAppearance, 0)
        val title = attributeSet.getString(R.styleable.InputItemLayout_title)
        parseTitleStyle(titleStyleId, title)

        //解析输入框资源属性
        val inputStyleId =
            attributeSet.getResourceId(R.styleable.InputItemLayout_titleTextAppearance, 0)
        val hint = attributeSet.getString(R.styleable.InputItemLayout_hint)
        val inputType = attributeSet.getInt(R.styleable.InputItemLayout_inputType, 0)
        val maxInputLength =
            attributeSet.getInt(R.styleable.InputItemLayout_maxInputLength, 20)
        parseInputStyle(inputStyleId, hint, inputType, maxInputLength)

        //解析上下 分割线资源属性
        val topLineStyleId =
            attributeSet.getResourceId(R.styleable.InputItemLayout_topLineAppearance, 0)
        val bottomLineStyleId =
            attributeSet.getResourceId(R.styleable.InputItemLayout_bottomLineAppearance, 0)
        topLineStyle = parseLineStyle(topLineStyleId)
        bottomLineStyle = parseLineStyle(bottomLineStyleId)

        if (topLineStyle.enable) {
            topPaint.color = topLineStyle.color
            topPaint.style = Paint.Style.FILL_AND_STROKE
            topPaint.strokeWidth = topLineStyle.height.toFloat()
        }

        if (bottomLineStyle.enable) {
            bottomPaint.color = bottomLineStyle.color
            bottomPaint.style = Paint.Style.FILL_AND_STROKE
            bottomPaint.strokeWidth = bottomLineStyle.height.toFloat()
        }


        attributeSet.recycle()
    }

    private fun parseLineStyle(resId: Int): Line {
        val line = Line()
        val lineAttributes =
            context.obtainStyledAttributes(resId, R.styleable.lineAppearance)
        line.color = lineAttributes.getColor(
            R.styleable.lineAppearance_color,
            resources.getColor(R.color.color_C1B)
        )
        line.height = lineAttributes.getDimensionPixelSize(R.styleable.lineAppearance_height, 0)
        line.leftMargin =
            lineAttributes.getDimensionPixelSize(R.styleable.lineAppearance_leftMargin, 0)
        line.rightMargin =
            lineAttributes.getDimensionPixelSize(R.styleable.lineAppearance_rightMargin, 0)
        line.enable = lineAttributes.getBoolean(R.styleable.lineAppearance_enabled, true)

        lineAttributes.recycle()

        return line
    }

    private fun parseInputStyle(resId: Int, hint: String?, inputType: Int, maxInputLength: Int) {
        val inputAttributeSet =
            context.obtainStyledAttributes(resId, R.styleable.inputTextAppearance)
        val hintColor = inputAttributeSet.getColor(
            R.styleable.inputTextAppearance_hintColor,
            resources.getColor(R.color.color_9b9)
        )
        val inputColor = inputAttributeSet.getColor(
            R.styleable.inputTextAppearance_inputColor,
            resources.getColor(R.color.color_565)
        )
        val testSize = inputAttributeSet.getDimensionPixelSize(
            R.styleable.inputTextAppearance_textSize,
            applyUnit(15f)
        )
        val editText = EditText(context)
        editText.setPadding(0, 0, 0, 0)
        editText.filters = arrayOf(InputFilter.LengthFilter(maxInputLength)) //最大可以输入的字符数
        val params = LayoutParams(LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT)
        params.weight = 1f
        editText.layoutParams = params

        editText.hint = hint
        editText.setHintTextColor(hintColor)
        editText.setTextColor(inputColor)
        editText.gravity = Gravity.START or (Gravity.CENTER)

        editText.setTextSize(TypedValue.COMPLEX_UNIT_PX, testSize.toFloat())

        editText.setBackgroundColor(Color.TRANSPARENT)
        when (inputType) {
            0 -> {
                editText.inputType = InputType.TYPE_CLASS_TEXT
            }
            1 -> {
                editText.inputType =
                    InputType.TYPE_TEXT_VARIATION_PASSWORD or InputType.TYPE_CLASS_TEXT
            }
            2 -> {
                editText.inputType = InputType.TYPE_CLASS_NUMBER
            }
        }
        addView(editText)
        inputAttributeSet.recycle()
    }

    private fun parseTitleStyle(resId: Int, title: String?) {
        val titleAttributeSet =
            context.obtainStyledAttributes(resId, R.styleable.titleTextAppearance)

        val titleColor = titleAttributeSet.getColor(
            R.styleable.titleTextAppearance_titleColor,
            resources.getColor(R.color.color_565)
        )

        val titleSize = titleAttributeSet.getDimensionPixelSize(
            R.styleable.titleTextAppearance_titleSize,
            applyUnit(15f)
        )
        val minWidth = titleAttributeSet.getDimensionPixelSize(
            R.styleable.titleTextAppearance_minWidth, 0
        )

        val titleView = TextView(context)
        titleView.setTextSize(TypedValue.COMPLEX_UNIT_PX, titleSize.toFloat())  //sp---当做sp在转换一次
        titleView.setTextColor(titleColor)

        val layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT)
        titleView.layoutParams = layoutParams
        titleView.minWidth = minWidth
        titleView.gravity = Gravity.START or Gravity.CENTER
        titleView.text = title

        addView(titleView)
        titleAttributeSet.recycle()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        if (topLineStyle.enable) {
            canvas?.drawLine(
                topLineStyle.leftMargin.toFloat(),
                0f,
                measuredWidth - topLineStyle.rightMargin.toFloat(),
                0F,
                topPaint
            )
        }

        if (bottomLineStyle.enable) {
            canvas?.drawLine(
                bottomLineStyle.leftMargin.toFloat(),
                height - bottomLineStyle.height.toFloat(),
                measuredWidth - bottomLineStyle.rightMargin.toFloat(),
                height - bottomLineStyle.height.toFloat(),
                bottomPaint
            )
        }
    }

    /**
     * 将unit 的转成px单位
     *
     * [unit]:需要转换的单位，比如sp，dp TypedValue.COMPLEX_UNIT_SP
     * [value]:转换的值
     * return: 转换结果
     */
    private fun applyUnit(value: Float, unit: Int = TypedValue.COMPLEX_UNIT_SP): Int {
        return TypedValue.applyDimension(unit, value, resources.displayMetrics).toInt()
    }

    internal class Line {

        var color: Int = 0
        var height: Int = 0
        var leftMargin: Int = 0
        var rightMargin: Int = 0
        var enable: Boolean = true
    }
}