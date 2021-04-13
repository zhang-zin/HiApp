package com.zj.common.ui.component;

import android.app.Application;

import com.zj.hi_library.hiLog.HiLogManager;

public class HiBaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        HiLogManager.init(this, false, 0);
    }
}
