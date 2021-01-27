package com.zj.hi_ui.ui.banner.core;

/**
 * HiBanner的数据绑定接口，基于该接口可以实现数据的绑定和框架层解耦
 *
 * @author 张锦
 */
public interface IBindAdapter {

    /**
     * 绑定adapter
     *
     * @param viewHolder 绑定的viewHolder
     * @param mo         绑定的数据
     * @param position   绑定的位置
     */
    void onBind(HiBannerAdapter.HiBannerViewHolder viewHolder, HiBannerMo mo, int position);
}
