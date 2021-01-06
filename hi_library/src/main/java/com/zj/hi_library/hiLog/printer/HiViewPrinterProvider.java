package com.zj.hi_library.hiLog.printer;

import android.app.Activity;
import android.app.Application;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.zj.hi_library.util.HiDisplayUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zhangjin
 */
public class HiViewPrinterProvider {

    private static RecycleViewCallback recycleViewCallback;
    private static HiViewPrinterProvider instance;

    private boolean isOpen;
    private final Application app;

    private static final String TAG_LOG_VIEW = "TAG_LOG_VIEW";
    private static final String TAG_FLOATING_VIEW = "TAG_FLOATING_VIEW";

    private Activity currentActivity;

    private final Map<String, View> floatingViews = new HashMap<>();
    private final Map<String, FrameLayout> logViews = new HashMap<>();
    private final Map<String, RecyclerView> recyclerViews = new HashMap<>();
    private final HiLogActivityLifecycleCallbacks callbacks = new HiLogActivityLifecycleCallbacks();

    private HiViewPrinterProvider(Application app) {
        this.app = app;
        app.registerActivityLifecycleCallbacks(callbacks);
    }

    public static HiViewPrinterProvider init(Application app, RecycleViewCallback callback) {
        if (instance == null) {
            instance = new HiViewPrinterProvider(app);
        }
        HiViewPrinterProvider.recycleViewCallback = callback;
        return instance;
    }

    public static HiViewPrinterProvider getInstance() {
        return instance;
    }

    /**
     * 添加HiLog
     *
     * @param activity 添加的activity
     */
    public void addView(Activity activity) {
        this.currentActivity = activity;
        FrameLayout rootView = activity.findViewById(android.R.id.content);

        if (rootView.findViewWithTag(TAG_FLOATING_VIEW) != null) {
            return;
        }
        FrameLayout.LayoutParams params =
                new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.BOTTOM | Gravity.END;
        View floatingView = genFloatingView();
        floatingView.setTag(TAG_FLOATING_VIEW);
        floatingView.setBackgroundColor(Color.BLACK);
        floatingView.setAlpha(0.8f);
        params.bottomMargin = HiDisplayUtil.dp2px(100, rootView.getResources());
        rootView.addView(floatingView, params);
        if (isOpen) {
            showLogView();
        }
    }

    private View genFloatingView() {
        String activityName = currentActivity.getClass().getName();
        if (floatingViews.get(activityName) != null) {
            return floatingViews.get(activityName);
        }

        TextView textView = new TextView(currentActivity);
        textView.setOnClickListener(v -> {
            if (!isOpen) {
                showLogView();
            }
        });
        textView.setText("HiLog");
        textView.setTextColor(Color.WHITE);
        floatingViews.put(activityName, textView);
        return textView;
    }

    private void showLogView() {
        FrameLayout rootView = currentActivity.findViewById(android.R.id.content);
        if (rootView.findViewWithTag(TAG_LOG_VIEW) != null) {
            return;
        }

        FrameLayout.LayoutParams params =
                new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, HiDisplayUtil.dp2px(160, rootView.getResources()));
        params.gravity = Gravity.BOTTOM;
        View logView = getLogView();
        logView.setTag(TAG_LOG_VIEW);
        rootView.addView(logView, params);
        isOpen = true;
        if (recycleViewCallback != null) {
            recycleViewCallback.showLogView();
        }
    }

    private View getLogView() {
        String activityName = currentActivity.getClass().getName();
        if (logViews.get(activityName) != null) {
            return logViews.get(activityName);
        }

        FrameLayout logView = new FrameLayout(currentActivity);
        logView.setBackgroundColor(Color.BLACK);

        RecyclerView recyclerView = new RecyclerView(currentActivity);
        logView.addView(recyclerView);
        FrameLayout.LayoutParams params =
                new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.END;
        TextView closeView = new TextView(currentActivity);
        closeView.setOnClickListener(v -> closeLogView());
        closeView.setText("Close");
        closeView.setTextColor(Color.WHITE);
        logView.addView(closeView, params);

        logViews.put(activityName, logView);
        recyclerViews.put(activityName, recyclerView);

        return logView;
    }

    private void closeLogView() {
        String activityName = currentActivity.getClass().getName();
        FrameLayout frameLayout = logViews.get(activityName);
        FrameLayout rootView = currentActivity.findViewById(android.R.id.content);
        rootView.removeView(frameLayout);
        isOpen = false;

        if (recycleViewCallback != null) {
            recycleViewCallback.closeLogView();
        }
    }

    public void removeView(Activity activity) {
        String name = activity.getClass().getName();
        FrameLayout rootView = activity.findViewById(android.R.id.content);
        View view = floatingViews.get(name);
        FrameLayout frameLayout = logViews.get(name);
        rootView.removeView(view);
        rootView.removeView(frameLayout);

    }

    public void isShowLogView() {
        if (isOpen) {
            showLogView();
        } else {
            closeLogView();
        }
    }

    protected RecyclerView getRecyclerView() {
        if (currentActivity == null) {
            return null;
        }
        String name = currentActivity.getClass().getName();
        return recyclerViews.get(name);
    }

    public void destroy() {
        logViews.clear();
        recyclerViews.clear();
        floatingViews.clear();
    }

    interface RecycleViewCallback {

        /**
         * 当logView显示要通知视图打印器，打印已经有的log
         */
        void showLogView();

        void closeLogView();
    }

}
