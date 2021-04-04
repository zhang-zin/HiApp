package com.zj.hi_ui.ui.banner.core;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import java.util.List;

/**
 * @author 张锦
 */
public class HiBannerAdapter extends PagerAdapter {

    private Context mContext;
    private SparseArray<HiBannerViewHolder> mCachedViews = new SparseArray<>();
    private IHiBanner.OnBannerClickListener onBannerClickListener;
    private IBindAdapter mBindAdapter;
    private List<? extends HiBannerMo> models;

    /**
     * 是否开启自动轮播
     */
    private boolean mAutoPlay = true;

    /**
     * 非自动轮播状态下是否可以循环切换
     */
    private boolean mLoop = false;
    private int mLayoutResId = -1;

    public HiBannerAdapter(@NonNull Context mContext) {
        this.mContext = mContext;
    }

    //region 设置数据

    public void setModels(@NonNull List<? extends HiBannerMo> models) {
        this.models = models;
        initCacheView();
        notifyDataSetChanged();
    }

    public void setBindAdapter(IBindAdapter mBindAdapter) {
        this.mBindAdapter = mBindAdapter;
    }

    public void setOnBannerClickListener(IHiBanner.OnBannerClickListener onBannerClickListener) {
        this.onBannerClickListener = onBannerClickListener;
    }

    public void setAutoPlay(boolean mAutoPlay) {
        this.mAutoPlay = mAutoPlay;
    }

    public void setLoop(boolean mLoop) {
        this.mLoop = mLoop;
    }

    public void setLayoutResId(int mLayoutResId) {
        this.mLayoutResId = mLayoutResId;
    }
    //endregion

    /**
     * 获取初次展示的item位置
     *
     * @return 初次展示的item位置
     */
    public int getFirstItem() {
        return Integer.MAX_VALUE / 2 - (Integer.MAX_VALUE / 2 % getRealCount());
    }

    @Override
    public int getCount() {
        return (mAutoPlay || mLoop) ? Integer.MAX_VALUE : getRealCount();
    }

    /**
     * 获取Banner页面数量
     *
     * @return Banner页面真实数量
     */
    public int getRealCount() {
        return models == null ? 0 : models.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        int realPosition = position;
        if (getRealCount() > 0) {
            realPosition = position % getRealCount();
        }
        HiBannerViewHolder viewHolder = mCachedViews.get(realPosition);
        if (container.equals(viewHolder.rootView.getParent())) {
            container.removeView(viewHolder.rootView);
        }
        onBind(viewHolder, models.get(realPosition), realPosition);
        if (viewHolder.rootView.getParent() != null) {
            ((ViewGroup) viewHolder.rootView.getParent()).removeView(viewHolder.rootView);
        }
        container.addView(viewHolder.rootView);
        return viewHolder.rootView;
    }

    protected void onBind(HiBannerViewHolder viewHolder, HiBannerMo hiBannerMo, int realPosition) {
        viewHolder.rootView.setOnClickListener(v -> {
            if (onBannerClickListener != null) {
                onBannerClickListener.onBannerClick(viewHolder, hiBannerMo, realPosition);
            }
        });
        if (mBindAdapter != null) {
            mBindAdapter.onBind(viewHolder, hiBannerMo, realPosition);
        }
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {

    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }

    private void initCacheView() {
        for (int i = 0; i < models.size(); i++) {
            HiBannerViewHolder viewHolder = new HiBannerViewHolder(createView(LayoutInflater.from(mContext), null));
            mCachedViews.put(i, viewHolder);
        }
    }

    private View createView(LayoutInflater from, ViewGroup parent) {
        if (mLayoutResId == -1) {
            throw new IllegalArgumentException("you must be set setLayoutResId first");
        }

        return from.inflate(mLayoutResId, parent, false);
    }

    public static class HiBannerViewHolder {

        private SparseArray<View> viewHolderSparseArr;

        View rootView;

        public HiBannerViewHolder(View rootView) {
            this.rootView = rootView;
        }

        public View getRootView() {
            return rootView;
        }

        public <V extends View> V findViewById(int id) {
            if (!(rootView instanceof ViewGroup)) {
                return (V) rootView;
            }
            if (viewHolderSparseArr == null) {
                viewHolderSparseArr = new SparseArray<>();
            }
            V childView = (V) viewHolderSparseArr.get(id);
            if (childView == null) {
                childView = rootView.findViewById(id);
                viewHolderSparseArr.put(id, childView);
            }
            return childView;
        }
    }
}
