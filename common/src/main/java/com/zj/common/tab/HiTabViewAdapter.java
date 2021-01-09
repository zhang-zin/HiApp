package com.zj.common.tab;

import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.zj.hi_ui.ui.tab.bottom.HiTabBottomInfo;

import java.util.List;

public class HiTabViewAdapter {

    private List<HiTabBottomInfo<?>> mInfoList;
    private Fragment mCurFragment;
    private FragmentManager mFragmentManager;

    public HiTabViewAdapter(FragmentManager fragmentManager, List<HiTabBottomInfo<?>> infoList) {
        this.mInfoList = infoList;
        this.mFragmentManager = fragmentManager;
    }

    /**
     * 实例化以及显示指定位置的Fragment
     *
     * @param container fragment添加的View
     * @param position  指定位置
     */
    public void instantiateItem(View container, int position) {
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();

        if (mCurFragment != null) {
            fragmentTransaction.hide(mCurFragment);
        }

        String name = container.getId() + ":" + position;
        Fragment fragment = mFragmentManager.findFragmentByTag(name);
        if (fragment != null) {
            fragmentTransaction.show(fragment);
        } else {
            fragment = getItem(position);
            if (!fragment.isAdded()) {
                fragmentTransaction.add(container.getId(), fragment, name);
            }
        }
        mCurFragment = fragment;
        fragmentTransaction.commitAllowingStateLoss();
    }

    public Fragment getItem(int position) {
        HiTabBottomInfo<?> hiTabBottomInfo = mInfoList.get(position);
        try {
            return hiTabBottomInfo.fragment.newInstance();
        } catch (IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Fragment getCurFragment() {
        return mCurFragment;
    }

    public int getCount() {
        return mInfoList.size();
    }
}
