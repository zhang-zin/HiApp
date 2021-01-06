package com.zj.hi_library.hiLog.printer;

import androidx.annotation.NonNull;

import com.zj.hi_library.hiLog.HiLogConfig;

import java.io.File;

/**
 * 文件打印器，日志信息输出到文件中
 */
public class HiFilePrinter implements HiLogPrinter {

    /**
     * 日志信息写入的文件夹
     */
    private final File hiLogFile;

    public HiFilePrinter(File hiLogFile) {
        this.hiLogFile = hiLogFile;
    }

    @Override
    public void print(@NonNull HiLogConfig config, int level, String tag, @NonNull String printString) {

    }
}
