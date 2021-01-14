package com.zj.hi_ui.ui.refresh;

/**
 * 刷新框架对外接口
 *
 * @author 张锦
 */
public interface HiRefresh {

    /**
     * 设置下拉刷新视图
     *
     * @param hiOverView 下拉刷视图
     */
    void setRefreshOverview(HiOverView hiOverView);

    /**
     * 下拉刷新监听器
     *
     * @param hiRefreshListener 监听器
     */
    void setRefreshListener(HiRefreshListener hiRefreshListener);

    /**
     * 刷新完成
     */
    void refreshFinished();

    /**
     * 刷新时是否禁止滑动
     *
     * @param disableRefreshScroll 是否禁止滑动
     */
    void setDisableRefreshScroll(boolean disableRefreshScroll);

    interface HiRefreshListener {

        /**
         * 刷新
         */
        void onRefresh();

        /**
         * 是否启用刷新
         *
         * @return 是否启用刷新，true启用
         */
        boolean enableRefresh();
    }
}
