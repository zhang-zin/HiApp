package com.zj.hi_ui.ui.refresh;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.zj.hi_library.util.HiDisplayUtil;

/**
 * 下拉刷新头布局
 *
 * @author 张锦
 */
public abstract class HiOverView extends FrameLayout {

    enum HiRefreshState {
        /**
         * 初始态
         */
        STATE_INIT,

        /**
         * Header展示出来的状态
         */
        STATE_VISIBLE,

        /**
         * 超出可刷新距离的状态
         */
        STATE_OVER,

        /**
         * 刷新中的状态
         */
        STATE_REFRESH,

        /**
         * 超出刷新位置松开手后的状态
         */
        STATE_OVER_RELEASE
    }

    private HiRefreshState mState = HiRefreshState.STATE_INIT;

    /**
     * 触发下拉刷新需要的最小高度
     */
    public int mPullRefreshHeight;

    /**
     * 最小阻尼
     */
    public float minDamp = 1.6f;

    /**
     * 最大阻尼，滑动越远越难滑动
     */
    public float maxDamp = 2.2f;

    public HiOverView(@NonNull Context context) {
        this(context, null);
    }

    public HiOverView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HiOverView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        preInit();
    }

    protected void preInit() {
        mPullRefreshHeight = HiDisplayUtil.dp2px(66, getResources());
        init();
    }

    /**
     * 初始化
     */
    abstract void init();

    /**
     * 开始滑动
     *
     * @param scrollY
     * @param pullRefreshHeight
     */
    abstract void onScroll(int scrollY, int pullRefreshHeight);

    /**
     * 显示Overlay
     */
    abstract void onVisible();

    /**
     * 超过Overlay，释放就会刷新
     */
    abstract void onOver();

    /**
     * 正在刷新
     */
    abstract void onRefresh();

    /**
     * 刷新完成
     */
    abstract void onFinish();

    void setState(HiRefreshState state) {
        this.mState = state;
    }

    HiRefreshState getState() {
        return mState;
    }
}
