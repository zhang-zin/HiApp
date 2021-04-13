package com.zj.hiapp.fragment;

import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;

import androidx.core.content.ContextCompat;

import com.zj.common.ui.component.HiBaseFragment;
import com.zj.hiapp.R;

public class ProfileFragment extends HiBaseFragment {
    @Override
    public int getLayoutId() {
        return R.layout.fragment_profile_page;
    }

    private CharSequence spannableTabItem(int topText, String bottomText) {
        String spanStr = String.valueOf(topText);
        SpannableStringBuilder ssb = new SpannableStringBuilder();
        SpannableString topSpannable = new SpannableString(spanStr);

        int spanFlag = Spannable.SPAN_EXCLUSIVE_EXCLUSIVE;

        topSpannable.setSpan(
                new ForegroundColorSpan(ContextCompat.getColor(getContext(), R.color.color_000)),
                0,
                topSpannable.length(),
                spanFlag
        );
        topSpannable.setSpan(
                new AbsoluteSizeSpan(18, true),
                0,
                topSpannable.length(),
                spanFlag);
        topSpannable.setSpan(new StyleSpan(Typeface.BOLD), 0, topSpannable.length(), spanFlag);
        ssb.append(topSpannable);
        ssb.append(bottomText);
        return ssb;
    }
}
