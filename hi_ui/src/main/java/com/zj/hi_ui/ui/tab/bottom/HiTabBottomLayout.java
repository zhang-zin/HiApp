package com.zj.hi_ui.ui.tab.bottom;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.RecyclerView;

import com.zj.hi_library.util.HiDisplayUtil;
import com.zj.hi_library.util.HiViewUtil;
import com.zj.hi_ui.R;
import com.zj.hi_ui.ui.tab.common.IHiTabLayout;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class HiTabBottomLayout extends FrameLayout implements IHiTabLayout<HiTabBottom, HiTabBottomInfo<?>> {

    private List<IHiTabLayout.OnTabSelectedListener<HiTabBottomInfo<?>>> tabSelectedChangeListeners = new ArrayList<>();
    private HiTabBottomInfo<?> selectedInfo;
    private float bottomAlpha = 1f;
    //TabBottom高度
    private static float tabBottomHeight = 50;
    //TabBottom的头部线条高度
    private float bottomLineHeight = 0.5f;
    //TabBottom的头部线条颜色
    private String bottomLineColor = "#dfe0e1";
    private List<HiTabBottomInfo<?>> infoList;

    private static final String TAG_TAB_BOTTOM = "TAG_TAB_BOTTOM";


    public HiTabBottomLayout(@NonNull Context context) {
        this(context, null);
    }

    public HiTabBottomLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HiTabBottomLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public HiTabBottom findTab(@NonNull HiTabBottomInfo<?> info) {
        ViewGroup ll = findViewWithTag(TAG_TAB_BOTTOM);
        for (int i = 0; i < ll.getChildCount(); i++) {
            View child = ll.getChildAt(i);
            if (child instanceof HiTabBottom) {
                HiTabBottom hiTabBottom = (HiTabBottom) child;
                if (hiTabBottom.getHiTabInfo() == info) {
                    return hiTabBottom;
                }
            }
        }
        return null;
    }

    @Override
    public void addTabSelectedChangeListener(OnTabSelectedListener<HiTabBottomInfo<?>> listener) {
        tabSelectedChangeListeners.add(listener);
    }

    @Override
    public void defaultSelected(@NonNull HiTabBottomInfo<?> defaultInfo) {
        onSelected(defaultInfo);
    }

    @Override
    public void inflateInfo(@NonNull List<HiTabBottomInfo<?>> infoList) {
        if (infoList.isEmpty()) {
            return;
        }
        this.infoList = infoList;
        // 移除之前已经添加的View
        for (int i = getChildCount() - 1; i > 0; i--) {
            removeViewAt(i);
        }

        selectedInfo = null;
        addBackground();

        Iterator<OnTabSelectedListener<HiTabBottomInfo<?>>> iterator = tabSelectedChangeListeners.iterator();
        while (iterator.hasNext()) {
            if (iterator.next() instanceof HiTabBottom) {
                iterator.remove();
            }
        }

        int height = HiDisplayUtil.dp2px(tabBottomHeight, getResources());
        FrameLayout ll = new FrameLayout(getContext());
        ll.setTag(TAG_TAB_BOTTOM);
        int width = HiDisplayUtil.getDisplayWidthInPx(getContext()) / infoList.size();
        for (int i = 0; i < infoList.size(); i++) {
            final HiTabBottomInfo<?> info = infoList.get(i);
            LayoutParams layoutParams = new LayoutParams(width, height);
            layoutParams.leftMargin = i * width;
            layoutParams.gravity = Gravity.BOTTOM;

            HiTabBottom tabBottom = new HiTabBottom(getContext());
            tabSelectedChangeListeners.add(tabBottom);
            tabBottom.setHiTabInfo(info);
            ll.addView(tabBottom, layoutParams);
            tabBottom.setOnClickListener(v -> onSelected(info));
        }

        LayoutParams flParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        flParams.gravity = Gravity.BOTTOM;
        addBottomLine();
        addView(ll, flParams);
        fixContentView();
    }

    private void addBottomLine() {
        View bottomLine = new View(getContext());
        bottomLine.setBackgroundColor(Color.parseColor(bottomLineColor));
        LayoutParams bottomLineParams =
                new LayoutParams(LayoutParams.MATCH_PARENT, HiDisplayUtil.dp2px(bottomLineHeight, getResources()));
        bottomLineParams.gravity = Gravity.BOTTOM;
        bottomLineParams.bottomMargin = HiDisplayUtil.dp2px(tabBottomHeight - bottomLineHeight, getResources());
        addView(bottomLine, bottomLineParams);
        bottomLine.setAlpha(bottomAlpha);
    }

    private void onSelected(HiTabBottomInfo<?> nextInfo) {
        for (OnTabSelectedListener<HiTabBottomInfo<?>> tabSelectedChangeListener : tabSelectedChangeListeners) {
            tabSelectedChangeListener.onTabSelectedChange(infoList.indexOf(nextInfo), selectedInfo, nextInfo);
        }

        selectedInfo = nextInfo;
    }

    private void addBackground() {
        View bg = LayoutInflater.from(getContext()).inflate(R.layout.hi_bottom_layout_bg, null);
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, HiDisplayUtil.dp2px(tabBottomHeight, getResources()));
        layoutParams.gravity = Gravity.BOTTOM;
        addView(bg, layoutParams);
        bg.setAlpha(bottomAlpha);
    }

    private void fixContentView() {
        if (!(getChildAt(0) instanceof ViewGroup)) {
            return;
        }
        ViewGroup rootView = (ViewGroup) getChildAt(0);
        ViewGroup targetView = HiViewUtil.findTypeView(rootView, RecyclerView.class);
        if (targetView == null) {
            targetView = HiViewUtil.findTypeView(rootView, ScrollView.class);
        }
        if (targetView == null) {
            targetView = HiViewUtil.findTypeView(rootView, AbsListView.class);
        }

        if (targetView == null) {
            targetView = HiViewUtil.findTypeView(rootView, NestedScrollView.class);
        }

        if (targetView != null) {
            targetView.setPadding(0, 0, 0, HiDisplayUtil.dp2px(tabBottomHeight, getResources()));
            targetView.setClipToPadding(false);
        }
    }

    public static void clipBottomPadding(ViewGroup targetView) {
        if (targetView != null) {
            targetView.setPadding(0, 0, 0, HiDisplayUtil.dp2px(tabBottomHeight));
            targetView.setClipToPadding(false);
        }
    }

    public void setTabAlpha(float tabAlpha) {
        this.bottomAlpha = tabAlpha;
    }

    public static void setTabBottomHeight(float tabHeight) {
        HiTabBottomLayout.tabBottomHeight = tabHeight;
    }

    public void setBottomLineHeight(float bottomLineHeight) {
        this.bottomLineHeight = bottomLineHeight;
    }

    public void setBottomLineColor(String bottomLineColor) {
        this.bottomLineColor = bottomLineColor;
    }
}
