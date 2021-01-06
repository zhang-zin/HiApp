package com.zj.hi_library.hiLog;

import com.zj.hi_library.hiLog.format.HiLogStackTrackFormatter;
import com.zj.hi_library.hiLog.format.HiLogThreadFormatter;
import com.zj.hi_library.hiLog.printer.HiLogPrinter;

public abstract class HiLogConfig {

    /**
     * 一行打印的最大长度
     */
    public static int MAX_LEN = 512;
    public  static HiLogThreadFormatter HI_LOG_THREAD_FORMATTER = new HiLogThreadFormatter();
    public  static HiLogStackTrackFormatter HI_LOG_STACK_TRACK_FORMATTER = new HiLogStackTrackFormatter();

    public JsonParser injectJsonParser() {
        return null;
    }

    /**
     * 全局log的tag
     *
     * @return tag
     */
    public String getGlobalTag() {
        return "HiLog";
    }

    /**
     * 是否使用log日志
     *
     * @return true 使用
     */
    public boolean enable() {
        return true;
    }

    /**
     * 日志信息是否包含线程信息
     *
     * @return true 是
     */
    public boolean includeThread() {
        return true;
    }

    /**
     * 打印日志包含栈的深度
     *
     * @return 默认5层
     */
    public int stackTraceDepth() {
        return 5;
    }

    /**
     * 是否在手机app上打印log
     */
    public boolean isViewPrinter() {
        return true;
    }

    public interface JsonParser {
        String toJson(Object src);
    }
}
