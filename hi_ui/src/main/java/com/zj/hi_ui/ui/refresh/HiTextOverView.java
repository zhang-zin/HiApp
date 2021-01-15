package com.zj.hi_ui.ui.refresh;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.zj.hi_ui.R;

/**
 * 刷新Text类型头布局
 *
 * @author 张锦
 */
public class HiTextOverView extends HiOverView {

    private TextView text;
    private View rotateView;

    public HiTextOverView(@NonNull Context context) {
        super(context);
    }

    public HiTextOverView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public HiTextOverView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.hi_refresh_text_over_view, this, true);
        text = findViewById(R.id.text);
        rotateView = findViewById(R.id.iv_rotate);
    }

    @Override
    void onScroll(int scrollY, int pullRefreshHeight) {

    }

    @Override
    void onVisible() {
        text.setText("下拉刷新");
    }

    @Override
    void onOver() {
        text.setText("松开刷新");
    }

    @Override
    void onRefresh() {
        text.setText("正在刷新...");
        Animation operatingAnim = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_anim);
        rotateView.startAnimation(operatingAnim);
    }

    @Override
    void onFinish() {
        rotateView.clearAnimation();
    }
}
