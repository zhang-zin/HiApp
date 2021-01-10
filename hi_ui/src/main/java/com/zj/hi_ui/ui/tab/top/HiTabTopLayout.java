package com.zj.hi_ui.ui.tab.top;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.zj.hi_library.util.HiDisplayUtil;
import com.zj.hi_ui.ui.tab.common.IHiTabLayout;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class HiTabTopLayout extends HorizontalScrollView implements IHiTabLayout<HiTabTop, HiTabTopInfo<?>> {

    private List<OnTabSelectedListener<HiTabTopInfo<?>>> tabSelectedChangeListeners = new ArrayList<>();
    private HiTabTopInfo<?> selectedInfo;
    private List<HiTabTopInfo<?>> infoList;


    public HiTabTopLayout(@NonNull Context context) {
        this(context, null);
    }

    public HiTabTopLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HiTabTopLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setVerticalScrollBarEnabled(false);
    }

    @Override
    public HiTabTop findTab(@NonNull HiTabTopInfo<?> info) {
        LinearLayout ll = getRootLayout(false);
        for (int i = 0; i < ll.getChildCount(); i++) {
            View child = ll.getChildAt(i);
            if (child instanceof HiTabTop) {
                HiTabTop hiTabTop = (HiTabTop) child;
                if (hiTabTop.getHiTabInfo() == info) {
                    return hiTabTop;
                }
            }
        }
        return null;
    }

    @Override
    public void addTabSelectedChangeListener(OnTabSelectedListener<HiTabTopInfo<?>> listener) {
        tabSelectedChangeListeners.add(listener);
    }

    @Override
    public void defaultSelected(@NonNull HiTabTopInfo<?> defaultInfo) {
        onSelected(defaultInfo);
    }

    @Override
    public void inflateInfo(@NonNull List<HiTabTopInfo<?>> infoList) {
        if (infoList.isEmpty()) {
            return;
        }
        this.infoList = infoList;
        // 移除之前已经添加的View
        for (int i = getChildCount() - 1; i > 0; i--) {
            removeViewAt(i);
        }

        selectedInfo = null;

        Iterator<OnTabSelectedListener<HiTabTopInfo<?>>> iterator = tabSelectedChangeListeners.iterator();
        while (iterator.hasNext()) {
            if (iterator.next() instanceof HiTabTop) {
                iterator.remove();
            }
        }

        LinearLayout linearLayout = getRootLayout(true);
        for (int i = 0; i < infoList.size(); i++) {
            final HiTabTopInfo<?> info = infoList.get(i);
            HiTabTop hiTabTop = new HiTabTop(getContext());
            hiTabTop.setHiTabInfo(info);
            tabSelectedChangeListeners.add(hiTabTop);
            linearLayout.addView(hiTabTop);
            hiTabTop.setOnClickListener(v -> onSelected(info));
        }

    }

    private LinearLayout getRootLayout(boolean clear) {
        LinearLayout rootView = (LinearLayout) getChildAt(0);
        if (rootView == null) {
            rootView = new LinearLayout(getContext());
            rootView.setOrientation(LinearLayout.HORIZONTAL);
            LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            addView(rootView, layoutParams);
        } else if (clear) {
            rootView.removeAllViews();
        }
        return rootView;
    }

    private void onSelected(HiTabTopInfo<?> nextInfo) {
        for (OnTabSelectedListener<HiTabTopInfo<?>> tabSelectedChangeListener : tabSelectedChangeListeners) {
            tabSelectedChangeListener.onTabSelectedChange(infoList.indexOf(nextInfo), selectedInfo, nextInfo);
        }
        selectedInfo = nextInfo;

        autoScroll(nextInfo);
    }

    int tabWith;

    /**
     * 自动滚动，实现点击的位置能够自动滚动以展示前后2个
     *
     * @param nextInfo 点击tab的info
     */
    private void autoScroll(HiTabTopInfo<?> nextInfo) {
        HiTabTop tab = findTab(nextInfo);
        if (tab == null) {
            return;
        }
        int index = infoList.indexOf(nextInfo);
        int[] loc = new int[2];
        //获取点击控件在屏幕的坐标
        tab.getLocationInWindow(loc);

        if (tabWith == 0) {
            tabWith = tab.getWidth();
        }

        //获取屏幕宽度
        int displayWidthInPx = HiDisplayUtil.getDisplayWidthInPx(getContext());
        int scrollWidth; //tabTopLayout滚动距离
        //判断点击了是屏幕的左侧还是右侧
        if (loc[0] + tabWith / 2 > displayWidthInPx / 2) {
            scrollWidth = rangeScrollWidth(index, 2);
        } else {
            scrollWidth = rangeScrollWidth(index, -2);
        }

        scrollTo(scrollWidth + getScrollX(), 0);
    }

    /**
     * 获取可滚动的范围
     *
     * @param index 从第几个开始
     * @param range 向前向后的范围
     * @return 可滚动范围
     */
    private int rangeScrollWidth(int index, int range) {
        int scrollWidth = 0;
        for (int i = 0; i < Math.abs(range); i++) {
            int next;
            if (range < 0) {
                next = range + i + index;
            } else {
                next = range - i + index;
            }
            if (next >= 0 && next < infoList.size()) {
                if (range < 0) {
                    scrollWidth -= scrollWidth(next, false);
                } else {
                    scrollWidth += scrollWidth(next, true);
                }
            }
        }
        return scrollWidth;
    }

    /**
     * 获取每个tab的滚动距离
     *
     * @param index   指定位置的控件
     * @param toRight 指定位置的控件是否位于屏幕右侧
     * @return 指定位置控件的滚动距离
     */
    private int scrollWidth(int index, boolean toRight) {
        HiTabTop target = findTab(infoList.get(index));
        if (target == null) {
            return 0;
        }
        Rect rect = new Rect();
        target.getLocalVisibleRect(rect);
        if (toRight) {
            //点击在屏幕右侧
            if (rect.right > tabWith) {
                //right坐标大于控件的宽度时，说明完全没有显示
                return tabWith;
            } else {
                //显示部分，减去已显示的宽度
                return tabWith - rect.right;
            }
        } else {
            //点击在屏幕左侧
            if (rect.left <= -tabWith) {
                //left坐标小于等于-控件的宽度，说明完全没有显示
                return tabWith;
            } else if (rect.left > 0) {
                return rect.left;
            }
        }
        return 0;
    }
}
