package com.zj.hi_library.hiLog.printer;

/**
 * 视图打印器的数据bean
 */
public class HiLogMo {

    protected int level;
    protected String tag;
    protected String time;
    protected String log;

    public HiLogMo(int level, String tag, String log, String time) {
        this.level = level;
        this.tag = "tag: " + tag;
        this.time = time;
        this.log = log;
    }

    public String flattenedLog() {
        return getFlattened() + "\n" + log;
    }

    public String getFlattened() {
        return time + '|' + level + '|' + tag + "|:";
    }
}
