package com.zj.hi_ui.ui.tab.common;

import androidx.annotation.NonNull;
import androidx.annotation.Px;

/**
 * HiTab对外接口
 *
 * @param <D>
 */
public interface IHiTab<D> extends IHiTabLayout.OnTabSelectedListener<D> {

    /**
     * 设置tab数据
     *
     * @param data 数据
     */
    void setHiTabInfo(@NonNull D data);

    /**
     * 动态修改某个tab的大小
     *
     * @param height 高度值
     */
    void resetHeight(@Px int height);
}
