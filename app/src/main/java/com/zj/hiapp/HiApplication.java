package com.zj.hiapp;

import android.content.Context;

import androidx.multidex.MultiDex;

import com.alibaba.android.arouter.launcher.ARouter;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;
import com.zj.common.ui.component.HiBaseApplication;

import dagger.hilt.android.HiltAndroidApp;

@HiltAndroidApp
public class HiApplication extends HiBaseApplication {

    @Override
    public void onCreate() {
        super.onCreate();

        initARouter();
        initBugly();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
//        Beta.installTinker();
    }

    private void initBugly() {
        Bugly.init(this, "ff71f8734e", true);
        //设置为测试机
        Bugly.setIsDevelopmentDevice(this, true);
    }

    private void initARouter() {
        if (BuildConfig.DEBUG) {
            ARouter.openLog();
            ARouter.openDebug();
        }

        ARouter.init(this);
    }
}
