package com.zj.hi_library.hiLog.printer;

import android.content.Context;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.TextView;

public class HiViewPrinterProvider {

    public HiViewPrinterProvider(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.flags = WindowManager.LayoutParams.TYPE_PHONE;
        layoutParams.gravity = Gravity.BOTTOM | Gravity.END;
        layoutParams.width = 100;
        layoutParams.height = 100;

        TextView textView = new TextView(context);
        textView.setText("open");
        windowManager.addView(textView, layoutParams);

    }
}
