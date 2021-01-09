package com.zj.common.ui.common;

import android.app.Application;

import com.zj.hi_library.hiLog.HiLogManager;

public class HiBaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        HiLogManager.init(this, true, 0);
    }
}
