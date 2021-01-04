package com.zj.hi_library.hiLog.format;

import com.zj.hi_library.hiLog.format.HiLogFormatter;

/**
 * 格式化堆栈信息
 */
public class HiLogStackTrackFormatter implements HiLogFormatter<StackTraceElement[]> {

    @Override
    public String format(StackTraceElement[] stackTrace) {
        StringBuilder sb = new StringBuilder();
        if (stackTrace == null || stackTrace.length == 0) {
            return "";
        } else if (stackTrace.length == 1) {
            return "\t-" + stackTrace[0].toString();
        } else {
            for (int i = 0, len = stackTrace.length; i < stackTrace.length; i++) {
                if (i == 0) {
                    sb.append("stackTrace:  \n");
                }
                if (i != len - 1) {
                    sb.append("\t├ ");
                    sb.append(stackTrace[i].toString());
                    sb.append("\n");
                } else {
                    sb.append("\t└ ");
                    sb.append(stackTrace[i].toString());
                }
            }
        }
        return sb.toString();
    }
}
