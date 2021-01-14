package com.zj.hi_ui.ui.refresh;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.Scroller;

import com.zj.hi_ui.ui.refresh.HiOverView.HiRefreshState;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * 刷新控件
 *
 * @author 张锦
 */
public class HiRefreshLayout extends FrameLayout implements HiRefresh {

    private HiOverView mHiOverView;
    private boolean mDisableRefreshScroll;
    private HiRefreshListener mHiRefreshListener;
    private HiOverView.HiRefreshState mState = HiOverView.HiRefreshState.STATE_INIT;

    private AutoScroller mAutoScroller;
    /**
     * 手势监听器
     */
    private GestureDetector mGestureDetector;

    /**
     * 最后下拉距离Y轴坐标
     */
    private int mLastY;

    /**
     * 刷新时是否是禁止滑动
     */
    private boolean disableRefreshScroll;

    public HiRefreshLayout(@NonNull Context context) {
        this(context, null);
    }

    public HiRefreshLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HiRefreshLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mGestureDetector = new GestureDetector(getContext(), hiGestureDetector);
        mAutoScroller = new AutoScroller();
    }

    @Override
    public void setRefreshOverview(HiOverView hiOverView) {
        this.mHiOverView = hiOverView;
    }

    @Override
    public void setRefreshListener(HiRefreshListener hiRefreshListener) {
        this.mHiRefreshListener = hiRefreshListener;
    }

    @Override
    public void refreshFinished() {

    }

    @Override
    public void setDisableRefreshScroll(boolean disableRefreshScroll) {
        this.mDisableRefreshScroll = disableRefreshScroll;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        //根据下拉刷新，头部位置松开手之后决定是回弹位置，还是触发刷新
        View head = getChildAt(0);
        if (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_CANCEL
                || ev.getAction() == MotionEvent.ACTION_POINTER_INDEX_MASK) {
            //松开手
            if (head.getBottom() > 0) {
                if (mState != HiOverView.HiRefreshState.STATE_REFRESH) {
                    //head不在原来的位置并且不是正在刷新的状态
                    recover(head.getBottom());
                    return false;
                }
                mLastY = 0;
            }
        }
        boolean consumed = mGestureDetector.onTouchEvent(ev);
        //有消费事件或者不在初始状态和正在刷新状态
        boolean consumeEvent = consumed || (mState != HiRefreshState.STATE_INIT && mState != HiRefreshState.STATE_REFRESH);
        if (consumeEvent && head.getBottom() != 0) {
            //让父类接受不到真实的事件
            ev.setAction(MotionEvent.ACTION_CANCEL);
            return super.dispatchTouchEvent(ev);
        }

        if (consumed) {
            return true;
        } else {
            return super.dispatchTouchEvent(ev);
        }

    }

    /**
     * 恢复到原来的位置
     *
     * @param dis 滑动距离
     */
    private void recover(int dis) {
        if (mHiRefreshListener != null && dis > 0) {
            // 滑动到指定位置
            mAutoScroller.recover(dis - mHiOverView.mPullRefreshHeight);
            mState = HiOverView.HiRefreshState.STATE_OVER_RELEASE;
        } else {
            mAutoScroller.recover(dis);
        }
    }

    HiGestureDetector hiGestureDetector = new HiGestureDetector() {
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            //没有启用刷新
            boolean enable = mHiRefreshListener != null && !mHiRefreshListener.enableRefresh();
            if (Math.abs(distanceX) > Math.abs(distanceX) || enable) {
                //发生了横向滑动，或者刷新被禁止不做处理
                return false;
            }
            if (disableRefreshScroll && mState == HiRefreshState.STATE_REFRESH) {
                //正在刷新并且在刷新时禁止了滑动
                return true;
            }
            View head = getChildAt(0);
            View child = HiScrollUtil.findScrollableChild(HiRefreshLayout.this);
            if (HiScrollUtil.childScrolled(child)) {
                //列表发生了滑动
                return false;
            }
            boolean refresh = mState != HiRefreshState.STATE_REFRESH || head.getBottom() <= mHiOverView.mPullRefreshHeight;
            boolean dropDown = head.getBottom() > 0 || distanceY <= 0.0f;
            //没有刷新或没有达到刷新距离，且头部已经滑出下拉
            if (refresh && dropDown) {
                if (mState != HiRefreshState.STATE_OVER_RELEASE) {
                    int speed;
                    //阻尼计算
                    if (child.getTop() < mHiOverView.mPullRefreshHeight) {
                        speed = (int) (mLastY / mHiOverView.minDamp);
                    } else {
                        speed = (int) (mLastY / mHiOverView.maxDamp);
                    }
                    boolean bool = moveDown(speed, true);
                    mLastY = (int) -distanceY;
                    return bool;
                }else {
                    return false;
                }
            } else {
                return false;
            }
        }
    };

    /**
     * 根据偏移量移动header与child
     *
     * @param offsetY 偏移量
     * @param nonAuto 是否非自动滚动触发
     * @return
     */
    private boolean moveDown(int offsetY, boolean nonAuto) {
        return false;
    }

    class AutoScroller implements Runnable {

        private int mLastY;
        private final Scroller mScroller;
        private boolean mIsFinished;

        public AutoScroller() {
            mScroller = new Scroller(getContext(), new LinearInterpolator());
            mIsFinished = true;
        }

        @Override
        public void run() {
            if (mScroller.computeScrollOffset()) {
                // 滑动还未完成
                // TODO: 2021/1/13 滑动到指定位置 mLastY - mScroller.getCurrX()
                mLastY = mScroller.getCurrY();
                post(this);
            } else {
                removeCallbacks(this);
                mIsFinished = true;
            }
        }

        /**
         * 触发滑动
         *
         * @param dis 滑动的距离
         */
        void recover(int dis) {
            if (dis <= 0) {
                return;
            }
            removeCallbacks(this);
            mLastY = 0;
            mIsFinished = false;
            mScroller.startScroll(0, 0, 0, dis, 300);
            post(this);
        }

        public boolean isIsFinished() {
            return mIsFinished;
        }
    }
}
