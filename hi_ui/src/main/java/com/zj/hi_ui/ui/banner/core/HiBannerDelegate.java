package com.zj.hi_ui.ui.banner.core;

import android.content.Context;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;

import com.zj.hi_ui.R;
import com.zj.hi_ui.ui.banner.HiBanner;

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

    private void init(int layoutId) {
        if (mAdapter == null){
            mAdapter = new HiBannerAdapter(mContext);
        }

    }

    @Override
    public void setAutoPlay(boolean autoPlay) {

    }

    @Override
    public void setLoop(boolean loop) {

    }

    @Override
    public void setIntervalTime(int intervalTime) {

    }

    @Override
    public void setBindAdapter(IBindAdapter bindAdapter) {

    }

    @Override
    public void setScrollDuration(int duration) {

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
        if (mOnPageChangeListener != null) {
            mOnPageChangeListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
        }
    }

    @Override
    public void onPageSelected(int position) {
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
