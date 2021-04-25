package com.zj.common.ui.component;

import android.os.Bundle;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

public abstract class HiBaseActivity<T extends ViewDataBinding> extends AppCompatActivity implements HiBaseActionInterface {

    protected T binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBar();
        binding = DataBindingUtil.setContentView(this, getLayoutId());
        if (binding == null) {
            setContentView(getLayoutId());
        }
        init();
        initData();
        initEvent();
    }

    protected void setStatusBar() {
    }

    protected void init() {
    }

    protected void initData() {
    }

    protected void initEvent() {
    }

    /**
     * 返回Activity布局
     *
     * @return 布局Id
     */
    @LayoutRes
    public abstract int getLayoutId();
}
