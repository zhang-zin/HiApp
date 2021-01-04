package com.zj.hi_library.hiLog;

import android.content.Context;

import androidx.annotation.NonNull;

import com.zj.hi_library.hiLog.printer.HiConsolePrinter;
import com.zj.hi_library.hiLog.printer.HiLogPrinter;
import com.zj.hi_library.hiLog.printer.HiViewPrinter;

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

    public static void init(Context context) {
        HiLogPrinter hiConsolePrinter = new HiConsolePrinter();
        HiLogConfig hiLogConfig = new HiLogConfig() {

        };
        init(hiLogConfig, hiConsolePrinter);
    }

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
