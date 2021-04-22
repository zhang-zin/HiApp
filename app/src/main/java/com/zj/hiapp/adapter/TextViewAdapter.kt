package com.zj.hiapp.adapter

import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.AbsoluteSizeSpan
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.zj.hiapp.R

@BindingAdapter(value = ["topText", "bottomText"], requireAll = true)
fun spannableTabItem(textView: TextView, topText: Int, bottomText: String) {
    val spanStr = topText.toString()
    val ssb = SpannableStringBuilder()
    val topSpannable = SpannableString(spanStr)
    val spanFlag = Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
    topSpannable.setSpan(
        ForegroundColorSpan(ContextCompat.getColor(textView.context, R.color.color_000)),
        0,
        topSpannable.length,
        spanFlag
    )
    topSpannable.setSpan(
        AbsoluteSizeSpan(18, true),
        0,
        topSpannable.length,
        spanFlag
    )
    topSpannable.setSpan(StyleSpan(Typeface.BOLD), 0, topSpannable.length, spanFlag)
    ssb.append(topSpannable)
    ssb.append("\n")
    ssb.append(bottomText)
    textView.text = ssb
}

