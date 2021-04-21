package com.zj.hiapp.logic;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.FragmentManager;

import com.zj.common.tab.HiFragmentTabView;
import com.zj.common.tab.HiTabViewAdapter;
import com.zj.hi_ui.ui.tab.bottom.HiTabBottomInfo;
import com.zj.hi_ui.ui.tab.bottom.HiTabBottomLayout;
import com.zj.hiapp.R;
import com.zj.hiapp.fragment.category.CategoryFragment;
import com.zj.hiapp.fragment.FavoriteFragment;
import com.zj.hiapp.fragment.HomePageFragment;
import com.zj.hiapp.fragment.account.ProfileFragment;
import com.zj.hiapp.fragment.RecommendFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivityLogic {

    private static final String SAVED_CURRENT_ID = "SAVED_CURRENT_ID";
    private HiFragmentTabView fragmentTabView;
    private HiTabBottomLayout hiTabBottomLayout;
    private List<HiTabBottomInfo<?>> infoList = new ArrayList<>();
    private ActivityProvider activityProvider;
    private int currentItemIndex;

    public MainActivityLogic(ActivityProvider activityProvider, @Nullable Bundle savedInstanceState) {
        this.activityProvider = activityProvider;

        if (savedInstanceState != null) {
            currentItemIndex = savedInstanceState.getInt(SAVED_CURRENT_ID);
        }

        initTabBottom();
    }

    private void initTabBottom() {
        hiTabBottomLayout = activityProvider.findViewById(R.id.tab_bottom_layout);
        hiTabBottomLayout.setAlpha(0.85f);

        initTabLayoutData();

        hiTabBottomLayout.inflateInfo(infoList);
        initFragmentTabView();
        hiTabBottomLayout.addTabSelectedChangeListener((index, prevInfo, nextInfo) -> {
            fragmentTabView.setCurrentPosition(index);
            MainActivityLogic.this.currentItemIndex = index;
        });
        hiTabBottomLayout.defaultSelected(infoList.get(currentItemIndex));
    }

    /**
     * 初始化TabLayout数据
     */
    private void initTabLayoutData() {

        int defaultColor = activityProvider.getResources().getColor(R.color.tabBottomDefaultColor);
        int tintColor = activityProvider.getResources().getColor(R.color.tabBottomTintColor);

        HiTabBottomInfo homeInfo = new HiTabBottomInfo<>(
                "首页",
                "fonts/iconfont.ttf",
                activityProvider.getString(R.string.if_home),
                null,
                defaultColor,
                tintColor
        );
        homeInfo.fragment = HomePageFragment.class;
        HiTabBottomInfo infoFavorite = new HiTabBottomInfo<>(
                "收藏",
                "fonts/iconfont.ttf",
                activityProvider.getString(R.string.if_favorite),
                null,
                defaultColor,
                tintColor
        );
        infoFavorite.fragment = FavoriteFragment.class;
        HiTabBottomInfo infoCategory = new HiTabBottomInfo<>(
                "分类",
                "fonts/iconfont.ttf",
                activityProvider.getString(R.string.if_category),
                null,
                defaultColor,
                tintColor
        );
        infoCategory.fragment = CategoryFragment.class;
        HiTabBottomInfo infoRecommend = new HiTabBottomInfo<>(
                "推荐",
                "fonts/iconfont.ttf",
                activityProvider.getString(R.string.if_recommend),
                null,
                defaultColor,
                tintColor
        );
        infoRecommend.fragment = RecommendFragment.class;
        HiTabBottomInfo infoProfile = new HiTabBottomInfo<>(
                "我的",
                "fonts/iconfont.ttf",
                activityProvider.getString(R.string.if_profile),
                null,
                defaultColor,
                tintColor
        );
        infoProfile.fragment = ProfileFragment.class;
        infoList.add(homeInfo);
        infoList.add(infoFavorite);
        infoList.add(infoCategory);
        infoList.add(infoRecommend);
        infoList.add(infoProfile);
    }

    private void initFragmentTabView() {
        HiTabViewAdapter adapter = new HiTabViewAdapter(activityProvider.getSupportFragmentManager(), infoList);
        fragmentTabView = activityProvider.findViewById(R.id.hi_tab_view);
        fragmentTabView.setAdapter(adapter);
    }

    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt(SAVED_CURRENT_ID, currentItemIndex);
    }

    public HiFragmentTabView getFragmentTabView() {
        return fragmentTabView;
    }

    public List<HiTabBottomInfo<?>> getInfoList() {
        return infoList;
    }

    public HiTabBottomLayout getHiTabBottomLayout() {
        return hiTabBottomLayout;
    }


    public interface ActivityProvider {

        <T extends View> T findViewById(@IdRes int id);

        Resources getResources();

        FragmentManager getSupportFragmentManager();

        String getString(@StringRes int resId);
    }
}
