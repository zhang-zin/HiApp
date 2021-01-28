package com.zj.hi_ui.ui.banner;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.zj.hi_ui.R;
import com.zj.hi_ui.ui.banner.core.HiBannerDelegate;
import com.zj.hi_ui.ui.banner.core.HiBannerMo;
import com.zj.hi_ui.ui.banner.core.IBindAdapter;
import com.zj.hi_ui.ui.banner.core.IHiBanner;
import com.zj.hi_ui.ui.banner.indicator.HiIndicator;

import java.util.List;

/**
 * @author 张锦
 */
public class HiBanner extends FrameLayout implements IHiBanner {

    private final HiBannerDelegate delegate;

    public HiBanner(@NonNull Context context) {
        this(context, null);
    }

    public HiBanner(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HiBanner(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        delegate = new HiBannerDelegate(getContext(), this);
        initCustomAttrs(context, attrs);
    }

    private void initCustomAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.HiBanner);
        boolean autoPlay = typedArray.getBoolean(R.styleable.HiBanner_autoPlay, true);
        boolean loop = typedArray.getBoolean(R.styleable.HiBanner_loop, false);
        int intervalTime = typedArray.getInt(R.styleable.HiBanner_intervalTime, -1);
        setAutoPlay(autoPlay);
        setLoop(loop);
        setIntervalTime(intervalTime);
        typedArray.recycle();
    }

    //region 具体实现交给代理

    @Override
    public void setBannerData(int layoutId, @NonNull List<? extends HiBannerMo> models) {
        delegate.setBannerData(layoutId, models);
    }

    @Override
    public void setBannerData(@NonNull List<? extends HiBannerMo> models) {
        delegate.setBannerData(models);
    }

    @Override
    public void setHiIndicator(HiIndicator hiIndicator) {
        delegate.setHiIndicator(hiIndicator);
    }

    @Override
    public void setAutoPlay(boolean autoPlay) {
        delegate.setAutoPlay(autoPlay);
    }

    @Override
    public void setLoop(boolean loop) {
        delegate.setLoop(loop);
    }

    @Override
    public void setIntervalTime(int intervalTime) {
        delegate.setIntervalTime(intervalTime);
    }

    @Override
    public void setBindAdapter(IBindAdapter bindAdapter) {
        delegate.setBindAdapter(bindAdapter);
    }

    @Override
    public void setScrollDuration(int duration) {
        delegate.setScrollDuration(duration);
    }

    @Override
    public void setOnPageChangeListener(ViewPager.OnPageChangeListener mOnPageChangeListener) {
        delegate.setOnPageChangeListener(mOnPageChangeListener);
    }

    @Override
    public void setOnBannerClickListener(OnBannerClickListener bannerClickListener) {
        delegate.setOnBannerClickListener(bannerClickListener);
    }
    //endregion
}
