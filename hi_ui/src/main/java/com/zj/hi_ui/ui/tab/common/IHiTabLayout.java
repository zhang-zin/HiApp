package com.zj.hi_ui.ui.tab.common;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

/**
 * HiTabBottom容器的对外接口
 */
public interface IHiTabLayout<Tab extends ViewGroup, D> {

    /**
     * 根据数据寻找对应的tab
     *
     * @param data 容器中的数据
     */
    Tab findTab(@NonNull D data);

    /**
     * 添加Tab切换监听器
     */
    void addTabSelectedChangeListener(OnTabSelectedListener<D> listener);

    /**
     * 设置默认的tab
     */
    void defaultSelected(@NonNull D defaultInfo);

    /**
     * 初始化TabLayout
     */
    void inflateInfo(@NonNull List<D> infoList);

    interface OnTabSelectedListener<D> {

        /**
         * @param index    当前选中tab的索引
         * @param prevInfo 上一个选中的tab
         * @param nextInfo 下一个选中的tab
         */
        void onTabSelectedChange(int index, @Nullable D prevInfo, @NonNull D nextInfo);
    }

}
