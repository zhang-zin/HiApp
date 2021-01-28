package com.zj.hi_ui.ui.banner.core;

import android.content.Context;
import android.widget.FrameLayout;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;

import com.zj.hi_ui.R;
import com.zj.hi_ui.ui.banner.HiBanner;
import com.zj.hi_ui.ui.banner.indicator.HiCircleIndicator;
import com.zj.hi_ui.ui.banner.indicator.HiIndicator;

import java.util.List;

/**
 * HiBanner的控制器
 * 辅助HiBanner完成各种功能的控制
 * 将HiBanner的一些逻辑内聚在这
 *
 * @author 张锦
 */
public class HiBannerDelegate implements ViewPager.OnPageChangeListener, IHiBanner {

    private final Context mContext;
    private final HiBanner mHiBanner;
    private ViewPager.OnPageChangeListener mOnPageChangeListener;
    private OnBannerClickListener mBannerClickListener;
    private List<? extends HiBannerMo> mHiBannerMos;
    private HiBannerAdapter mAdapter;
    private boolean mAutoPlay;
    private boolean mLoop;
    private int mIntervalTime = 5000;
    private HiViewPager mHiViewPager;
    private HiIndicator mHiIndicator;
    private int mScrollDuration = -1;

    public HiBannerDelegate(Context context, HiBanner hiBanner) {
        this.mContext = context;
        this.mHiBanner = hiBanner;
    }

    @Override
    public void setBannerData(@NonNull List<? extends HiBannerMo> models) {
        setBannerData(R.layout.hi_banner_item_image, models);
    }

    @Override
    public void setBannerData(@LayoutRes int layoutId, @NonNull List<? extends HiBannerMo> models) {
        mHiBannerMos = models;
        init(layoutId);
    }

    @Override
    public void setHiIndicator(HiIndicator hiIndicator) {
        this.mHiIndicator = hiIndicator;
    }

    private void init(int layoutId) {
        if (mAdapter == null) {
            mAdapter = new HiBannerAdapter(mContext);
        }

        if (mHiIndicator == null) {
            mHiIndicator = new HiCircleIndicator(mContext);
        }
        mHiIndicator.onInflate(mHiBannerMos.size());

        mAdapter.setLayoutResId(layoutId);
        mAdapter.setModels(mHiBannerMos);
        mAdapter.setAutoPlay(mAutoPlay);
        mAdapter.setLoop(mLoop);
        mAdapter.setOnBannerClickListener(mBannerClickListener);

        mHiViewPager = new HiViewPager(mContext);
        mHiViewPager.setIntervalTime(mIntervalTime);
        mHiViewPager.setAutoPlay(mAutoPlay);
        mHiViewPager.addOnPageChangeListener(this);
        if (mScrollDuration > 0) mHiViewPager.setScrollDuration(mScrollDuration);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        mHiViewPager.setAdapter(mAdapter);

        if ((mLoop || mAutoPlay) && mAdapter.getRealCount() != 0) {
            int firstItem = mAdapter.getFirstItem();
            mHiViewPager.setCurrentItem(firstItem, false);
        }

        mHiBanner.removeAllViews();
        mHiBanner.addView(mHiViewPager, layoutParams);
        mHiBanner.addView(mHiIndicator.get(), layoutParams);
    }

    @Override
    public void setAutoPlay(boolean autoPlay) {
        mAutoPlay = autoPlay;
        if (mHiViewPager != null) {
            mHiViewPager.setAutoPlay(mAutoPlay);
        }
        if (mAdapter != null) {
            mAdapter.setAutoPlay(autoPlay);
        }
    }

    @Override
    public void setLoop(boolean loop) {
        mLoop = loop;
    }

    @Override
    public void setIntervalTime(int intervalTime) {
        if (intervalTime > 0) {
            mIntervalTime = intervalTime;
        }
    }

    @Override
    public void setBindAdapter(IBindAdapter bindAdapter) {
        mAdapter.setBindAdapter(bindAdapter);
    }

    @Override
    public void setScrollDuration(int duration) {
        mScrollDuration = duration;
        if (mHiViewPager != null && duration > 0) {

            mHiViewPager.setScrollDuration(duration);
        }
    }

    @Override
    public void setOnPageChangeListener(ViewPager.OnPageChangeListener mOnPageChangeListener) {
        this.mOnPageChangeListener = mOnPageChangeListener;
    }

    @Override
    public void setOnBannerClickListener(OnBannerClickListener bannerClickListener) {
        mBannerClickListener = bannerClickListener;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (mOnPageChangeListener != null && mAdapter != null && mAdapter.getRealCount() != 0) {
            mOnPageChangeListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
        }
    }

    @Override
    public void onPageSelected(int position) {
        if (mAdapter == null) {
            return;
        }

        if (mAdapter.getRealCount() == 0) {
            return;
        }

        position = position % mAdapter.getRealCount();
        if (mHiIndicator != null) {
            mHiIndicator.onPointChange(position, mAdapter.getRealCount());
        }

        if (mOnPageChangeListener != null) {
            mOnPageChangeListener.onPageSelected(position);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if (mOnPageChangeListener != null) {
            mOnPageChangeListener.onPageScrollStateChanged(state);
        }
    }

}
