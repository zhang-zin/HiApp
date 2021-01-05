package com.zj.hi_library.hiLog.printer;

import android.app.Activity;
import android.app.Application;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * activity生命周期回调
 *
 * @author zhangjin
 */
public class HiLogActivityLifecycleCallbacks implements Application.ActivityLifecycleCallbacks {

    private int startedActivityCounts;

    public HiLogActivityLifecycleCallbacks() {
    }

    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {

    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {
        startedActivityCounts++;
        HiViewPrinterProvider instance = HiViewPrinterProvider.getInstance();
        if (instance != null) {
            instance.addView(activity);
        }
    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {
        HiViewPrinterProvider instance = HiViewPrinterProvider.getInstance();
        if (instance!=null){
            instance.isShowLogView();
        }
    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {

    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {
        startedActivityCounts--;

    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {
        HiViewPrinterProvider instance = HiViewPrinterProvider.getInstance();
        if (instance != null) {
            instance.removeView(activity);
        }
        if (startedActivityCounts == 0 && instance != null) {
            instance.destroy();
        }
    }


    /**
     * 是否是系统main activity
     *
     * @return boolean
     */
    public boolean isMainLaunchActivity(Activity activity) {
        PackageManager packageManager = activity.getApplication().getPackageManager();
        Intent intent = packageManager.getLaunchIntentForPackage(activity.getPackageName());
        if (intent == null) {
            return false;
        }
        ComponentName launchComponentName = intent.getComponent();
        ComponentName componentName = activity.getComponentName();
        return launchComponentName != null && componentName.toString().equals(launchComponentName.toString());
    }
}
