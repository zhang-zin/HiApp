package com.zj.hi_ui.ui.banner.indicator;

import android.view.View;

/**
 * 指示器统一接口
 * 实现该接口定义需要的指示器
 *
 * @author 张锦
 */
public interface HiIndicator<T extends View> {

    /**
     * 获取指示器类型
     *
     * @return 指示器类型
     */
    T get();

    /**
     * 初始化Indicator
     *
     * @param count 数量
     */
    void onInflate(int count);

    /**
     * 切换指示器
     *
     * @param current 切换的位置
     * @param count   数量
     */
    void onPointChange(int current, int count);

}
