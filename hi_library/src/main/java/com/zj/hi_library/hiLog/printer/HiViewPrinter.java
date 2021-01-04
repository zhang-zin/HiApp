package com.zj.hi_library.hiLog.printer;

import android.content.Context;

import androidx.annotation.NonNull;

import com.zj.hi_library.hiLog.HiLogConfig;

/**
 * 在app界面上显示
 */
public class HiViewPrinter implements HiLogPrinter {

    private final HiViewPrinterProvider hiViewPrinterProvider;

    public HiViewPrinter(Context context) {
        hiViewPrinterProvider = new HiViewPrinterProvider(context);
    }

    @Override
    public void print(@NonNull HiLogConfig config, int level, String tag, @NonNull String printString) {

    }
}
