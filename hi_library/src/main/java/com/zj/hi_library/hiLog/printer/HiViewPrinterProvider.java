package com.zj.hi_library.hiLog.printer;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Build;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.TextView;

/**
 * @author zhangjin
 */
public class HiViewPrinterProvider {

    private static HiViewPrinterProvider instance;

    private WindowManager windowManager;
    private WindowManager.LayoutParams layoutParams;

    private final Application app;
    private final HiLogActivityLifecycleCallbacks callbacks = new HiLogActivityLifecycleCallbacks();

    private HiViewPrinterProvider(Application app) {
        this.app = app;
        app.registerActivityLifecycleCallbacks(callbacks);
        layoutParams = new WindowManager.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT, 0, 0, PixelFormat.TRANSLUCENT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            layoutParams.flags =(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
            | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
            | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
            layoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ERROR;
            layoutParams.gravity = Gravity.LEFT | Gravity.TOP;
            layoutParams.x = 500;
            layoutParams.y = 500;
        }
    }

    public static HiViewPrinterProvider init(Application app) {
        if (instance == null) {
            instance = new HiViewPrinterProvider(app);
        }
        return instance;
    }

    public static HiViewPrinterProvider getInstance() {
        return instance;
    }

    public WindowManager getWindowManager() {
        return windowManager;
    }

    public void addView(Activity activity) {
        windowManager = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
        TextView textView = new TextView(activity);
        textView.setText("open");
        textView.setTextColor(Color.RED);
        getWindowManager().addView(textView, layoutParams);
    }

    public void removeView(Activity activity) {
        app.unregisterActivityLifecycleCallbacks(callbacks);
    }
}
