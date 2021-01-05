package com.zj.hi_library.hiLog.printer;

import android.app.Application;

import androidx.annotation.NonNull;

import com.zj.hi_library.hiLog.HiLogConfig;

/**
 * 在app界面上显示
 *
 * @author zhangjin
 */
public class HiViewPrinter implements HiLogPrinter {

    private final HiViewPrinterProvider hiViewPrinterProvider;

    public HiViewPrinter(Application app) {
        hiViewPrinterProvider = HiViewPrinterProvider.init(app);
    }

    @Override
    public void print(@NonNull HiLogConfig config, int level, String tag, @NonNull String printString) {

    }
}
