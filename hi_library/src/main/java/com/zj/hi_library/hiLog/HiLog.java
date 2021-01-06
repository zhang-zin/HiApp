package com.zj.hi_library.hiLog;

import androidx.annotation.NonNull;

import com.zj.hi_library.hiLog.format.HiStackTrackUtil;
import com.zj.hi_library.hiLog.printer.HiLogPrinter;

import java.util.Arrays;
import java.util.List;

public class HiLog {

    private static final String HI_LOG_PACKAGE;

    static {
        String className = HiLog.class.getName();
        HI_LOG_PACKAGE = className.substring(0, className.lastIndexOf('.') + 1);
    }

    //region打印日志

    public static void v(@NonNull Object... contents) {
        log(HiLogType.V, contents);
    }

    public static void vT(String tag, @NonNull Object... contents) {
        log(HiLogType.V, tag, contents);
    }

    public static void d(@NonNull Object... contents) {
        log(HiLogType.D, contents);
    }

    public static void dT(String tag, @NonNull Object... contents) {
        log(HiLogType.D, tag, contents);
    }

    public static void i(@NonNull Object... contents) {
        log(HiLogType.I, contents);
    }

    public static void iT(String tag, @NonNull Object... contents) {
        log(HiLogType.I, tag, contents);
    }

    public static void w(@NonNull Object... contents) {
        log(HiLogType.W, contents);
    }

    public static void wT(String tag, @NonNull Object... contents) {
        log(HiLogType.W, tag, contents);
    }

    public static void e(@NonNull Object... contents) {
        log(HiLogType.E, contents);
    }

    public static void eT(String tag, @NonNull Object... contents) {
        log(HiLogType.E, tag, contents);
    }

    public static void a(@NonNull Object... contents) {
        log(HiLogType.A, contents);
    }

    public static void aT(String tag, @NonNull Object... contents) {
        log(HiLogType.A, tag, contents);
    }
    //endregion

    private static void log(@HiLogType.TYPE int type, Object... contents) {
        log(type, HiLogManager.getInstance().getConfig().getGlobalTag(), contents);
    }

    private static void log(@HiLogType.TYPE int type, String tag, Object... contents) {
        log(HiLogManager.getInstance().getConfig(), type, tag, contents);
    }

    private static void log(HiLogConfig config, int type, String tag, Object... contents) {
        if (!config.enable()) {
            return;
        }
        StringBuilder sb = new StringBuilder();
        if (config.includeThread()) {
            String threadInfo = HiLogConfig.HI_LOG_THREAD_FORMATTER.format(Thread.currentThread());
            sb.append(threadInfo).append("\n");
        }

        if (config.stackTraceDepth() > 0) {
            StackTraceElement[] stackTrace = new Throwable().getStackTrace();
            StackTraceElement[] croppedRealStackTrack = HiStackTrackUtil.getCroppedRealStackTrack(stackTrace, HI_LOG_PACKAGE, config.stackTraceDepth());
            String stackTrack = HiLogConfig.HI_LOG_STACK_TRACK_FORMATTER.format(croppedRealStackTrack);
            sb.append(stackTrack).append("\n");
        }

        String body = parseBody(contents, config);
        if (body != null) {
            body = body.replace("\\\"", "\"");
        }
        sb.append(body);

        List<HiLogPrinter> hiLogPrinters = HiLogManager.getInstance().getPrinterList();
        for (HiLogPrinter hiLogPrinter : hiLogPrinters) {
            hiLogPrinter.print(config, type, tag, sb.toString());
        }
    }

    private static String parseBody(@NonNull Object[] contents, HiLogConfig config) {

        if (config.injectJsonParser() != null) {
            if (contents.length == 1 && contents[0] instanceof String) {
                return (String) contents[0];
            }
            return config.injectJsonParser().toJson(contents);
        }

        StringBuilder sb = new StringBuilder();
        for (Object content : contents) {
            sb.append(content.toString()).append(";");
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }

}
