package com.zj.hi_library.hiLog;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;

import com.zj.hi_library.BuildConfig;
import com.zj.hi_library.hiLog.printer.HiConsolePrinter;
import com.zj.hi_library.hiLog.printer.HiFilePrinter;
import com.zj.hi_library.hiLog.printer.HiLogPrinter;
import com.zj.hi_library.hiLog.printer.HiViewPrinter;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * HiLog管理器
 */
public class HiLogManager {

    private HiLogConfig config;
    private static HiLogManager instance;
    private List<HiLogPrinter> printerList = new ArrayList<>();

    private HiLogManager(HiLogConfig config, HiLogPrinter[] printer) {
        this.config = config;
        this.printerList.addAll(Arrays.asList(printer));
    }

    public static HiLogManager getInstance() {
        return instance;
    }

    /**
     * 初始化日志打印控制器
     *
     * @param app           app
     * @param isViewPrinter 是否启动视图打印器 true：启动在应用上显示日志信息
     */
    public static void init(Application app, boolean isViewPrinter) {
        HiLogPrinter hiConsolePrinter = new HiConsolePrinter();
        HiLogPrinter hiFilePrinter = new HiFilePrinter(app.getDir("hiLogFile", Context.MODE_APPEND));
        HiLogConfig hiLogConfig = new HiLogConfig() {
            @Override
            public boolean isViewPrinter() {
                return isViewPrinter;
            }
        };
        if (hiLogConfig.isViewPrinter()) {
            HiLogPrinter hiViewPrinter = new HiViewPrinter(app);
            init(hiLogConfig, hiConsolePrinter, hiViewPrinter, hiFilePrinter);
        } else {
            init(hiLogConfig, hiConsolePrinter, hiFilePrinter);
        }
    }

    /**
     * 初始化日志打印控制器
     *
     * @param config  配置设置
     * @param printer 打印器，默认有三种实现  {@link HiViewPrinter}  {@link HiConsolePrinter}
     */
    public static void init(@NonNull HiLogConfig config, HiLogPrinter... printer) {
        instance = new HiLogManager(config, printer);
    }

    public void addPrinter(HiLogPrinter printer) {
        printerList.add(printer);
    }

    public void removePrinter(HiLogPrinter printer) {
        if (printerList != null && printerList.size() > 0) {
            printerList.remove(printer);
        }
    }

    public HiLogConfig getConfig() {
        return config;
    }

    public List<HiLogPrinter> getPrinterList() {
        return printerList;
    }
}
