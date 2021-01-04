package com.zj.hi_library.hiLog.format;

import com.zj.hi_library.hiLog.format.HiLogFormatter;

public class HiLogThreadFormatter implements HiLogFormatter<Thread> {
    @Override
    public String format(Thread data) {
        return "Thread: " + data.getName();
    }
}
