package com.zj.hi_ui.ui.banner.core;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;

import java.util.List;

/**
 * HiBanner对外接口
 *
 * @author 张锦
 */
public interface IHiBanner {

    /**
     * 设置banner数据
     *
     * @param layoutId banner布局文件id
     * @param models   item集合
     */
    void setBannerData(@LayoutRes int layoutId, @NonNull List<? extends HiBannerMo> models);

    /**
     * 设置banner数据
     *
     * @param models item集合
     */
    void setBannerData(@NonNull List<? extends HiBannerMo> models);

    /**
     * 设置是否自动滑动
     *
     * @param autoPlay 是否自动滑动
     */
    void setAutoPlay(boolean autoPlay);

    /**
     * 设置是否无限轮播
     *
     * @param loop 是否无限轮播
     */
    void setLoop(boolean loop);

    /**
     * 设置自动滑动时间间隔
     *
     * @param intervalTime 滑动间隔
     */
    void setIntervalTime(int intervalTime);

    /**
     * 数据绑定
     *
     * @param bindAdapter HiBanner的数据绑定接口
     */
    void setBindAdapter(IBindAdapter bindAdapter);

    /**
     * 设置滑动时长
     *
     * @param duration 滑动时长
     */
    void setScrollDuration(int duration);

    /**
     * 设置viewPage切换item监听
     *
     * @param mOnPageChangeListener 切换item监听
     */
    void setOnPageChangeListener(ViewPager.OnPageChangeListener mOnPageChangeListener);

    /**
     * 设置banner点击监听
     *
     * @param bannerClickListener banner点击监听
     */
    void setOnBannerClickListener(OnBannerClickListener bannerClickListener);

    interface OnBannerClickListener {
        /**
         * banner的item点击
         *
         * @param viewHolder 点击的viewHolder
         * @param hiBannerMo 点击的item
         * @param position   点击的位置
         */
        void onBannerClick(@NonNull HiBannerAdapter.HiBannerViewHolder viewHolder, @NonNull HiBannerMo hiBannerMo, int position);
    }
}
