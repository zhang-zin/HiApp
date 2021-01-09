package com.zj.common.tab;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * 将Fragment操作内聚
 */
public class HiFragmentTabView extends FrameLayout {

    private HiTabViewAdapter mAdapter;
    private int mCurrentPosition;

    public HiFragmentTabView(@NonNull Context context) {
        super(context);
    }

    public HiFragmentTabView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public HiFragmentTabView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public HiTabViewAdapter getAdapter() {
        return mAdapter;
    }

    public void setAdapter(HiTabViewAdapter adapter) {
        if (adapter == null || mAdapter != null) {
            return;
        }

        mCurrentPosition = -1;
        this.mAdapter = adapter;
    }

    public void setCurrentPosition(int position) {
        if (position < 0 || position >= mAdapter.getCount()) {
            return;
        }

        if (mCurrentPosition != position) {
            mCurrentPosition = position;
            mAdapter.instantiateItem(this, position);
        }
    }

    public int getCurrentPosition() {
        return mCurrentPosition;
    }

    public Fragment getCurrentFragment() {
        if (this.mAdapter == null) {
            throw new IllegalArgumentException("please call setAdapter first.");
        }
        return mAdapter.getCurFragment();
    }
}
