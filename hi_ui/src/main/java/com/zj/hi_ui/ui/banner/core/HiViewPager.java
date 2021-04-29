package com.zj.hi_ui.ui.banner.core;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import java.lang.reflect.Field;

/**
 * 自动翻页的viewPager
 *
 * @author 张锦
 */
public class HiViewPager extends ViewPager {

    private int mIntervalTime;
    /**
     * 是否自动播放
     */
    private boolean mAutoPlay;
    private boolean isLayout;

    private Handler mHandler = new Handler();
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            next();
            mHandler.postDelayed(this, mIntervalTime);
        }
    };

    public HiViewPager(@NonNull Context context) {
        super(context);
    }

    public HiViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                start();
                break;
            default:
                stop();
                break;
        }
        return super.onTouchEvent(ev);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        isLayout = true;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (isLayout && getAdapter() != null && getAdapter().getCount() > 1) {
            try {
                //fix 使用RecyclerView + ViewPager bug https://blog.csdn.net/u011002668/article/details/72884893
                Field mFirstLayout = ViewPager.class.getDeclaredField("mFirstLayout");
                mFirstLayout.setAccessible(true);
                mFirstLayout.set(this, false);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        start();
    }

    @Override
    protected void onDetachedFromWindow() {
        if (getContext() instanceof Activity && ((Activity) getContext()).isFinishing()) {
            super.onDetachedFromWindow();
        }
        // 防止内存泄漏
        stop();
    }

    public void setIntervalTime(int intervalTime) {
        this.mIntervalTime = intervalTime;
    }

    public void setAutoPlay(boolean autoPlay) {
        mAutoPlay = autoPlay;
        if (!mAutoPlay) {
            mHandler.removeCallbacks(mRunnable);
        }
    }

    public void start() {
        mHandler.removeCallbacksAndMessages(null);
        if (mAutoPlay) {
            mHandler.postDelayed(mRunnable, mIntervalTime);
        }
    }

    public void stop() {
        mHandler.removeCallbacksAndMessages(null);
    }

    /**
     * 设置下一个要显示的item，并且返回item的position
     *
     * @return 下一个要显示的position
     */
    private int next() {
        int nextPosition = -1;
        if (getAdapter() == null || getAdapter().getCount() <= 1) {
            stop();
            return nextPosition;
        }
        nextPosition = getCurrentItem() + 1;
        if (nextPosition >= getAdapter().getCount()) {
            //获取第一个item的索引
            nextPosition = ((HiBannerAdapter) getAdapter()).getFirstItem();
        }
        setCurrentItem(nextPosition, true);
        return nextPosition;
    }

    public void setScrollDuration(int duration) {
        try {
            Field mScroller = ViewPager.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true);
            HiBannerScroller hiBannerScroller = new HiBannerScroller(getContext(), duration);
            mScroller.set(this, hiBannerScroller);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

    }
}
