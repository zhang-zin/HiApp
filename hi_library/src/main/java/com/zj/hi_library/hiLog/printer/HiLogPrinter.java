package com.zj.hi_library.hiLog.printer;

import androidx.annotation.NonNull;

import com.zj.hi_library.hiLog.HiLogConfig;

/**
 * 打印器接口
 */
public interface HiLogPrinter {

    void print(@NonNull HiLogConfig config, int level, String tag, @NonNull String printString);
}
