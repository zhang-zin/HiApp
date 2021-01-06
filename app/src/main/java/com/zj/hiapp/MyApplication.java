package com.zj.hiapp;

import android.app.Application;

import com.zj.hi_library.hiLog.HiLogManager;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        HiLogManager.init(this,true);
    }
}
