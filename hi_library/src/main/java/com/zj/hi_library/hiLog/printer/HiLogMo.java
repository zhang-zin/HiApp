package com.zj.hi_library.hiLog.printer;

/**
 * 视图打印器的数据bean
 */
public class HiLogMo {

    protected int level;
    protected String tag;
    protected String time;
    protected String printString;

    public HiLogMo(int level, String tag, String printString, String time) {
        this.level = level;
        this.tag = "tag: " + tag;
        this.time = time;
        this.printString = printString;
    }
}
