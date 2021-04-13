package com.zj.hiapp;

import com.alibaba.android.arouter.launcher.ARouter;
import com.zj.common.ui.component.HiBaseApplication;

public class HiApplication extends HiBaseApplication {

    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            ARouter.openLog();
            ARouter.openDebug();
        }

        ARouter.init(this);
    }
}
