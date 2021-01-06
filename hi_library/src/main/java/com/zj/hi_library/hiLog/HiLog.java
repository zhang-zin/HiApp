package com.zj.hi_library.hiLog;

import androidx.annotation.NonNull;

import com.zj.hi_library.hiLog.format.HiStackTrackUtil;
import com.zj.hi_library.hiLog.printer.HiFilePrinter;
import com.zj.hi_library.hiLog.printer.HiLogPrinter;

import java.util.List;

public class HiLog {

    private static final String HI_LOG_PACKAGE;

    static {
        String className = HiLog.class.getName();
        HI_LOG_PACKAGE = className.substring(0, className.lastIndexOf('.') + 1);
    }

    //region打印日志

    public static void v(@NonNull Object... contents) {
        vF(false, contents);
    }

    public static void vT(String tag, @NonNull Object... contents) {
        vF(tag, false, contents);
    }

    public static void vF(boolean isWriterFile, @NonNull Object... contents) {
        log(HiLogType.V, isWriterFile, contents);
    }

    /**
     * @param tag          日志tag
     * @param isWriterFile 日志是否写入文件
     * @param contents     日志内容
     */
    public static void vF(String tag, boolean isWriterFile, @NonNull Object... contents) {
        log(HiLogType.V, tag, isWriterFile, contents);
    }

    public static void d(@NonNull Object... contents) {
        dF(false, contents);
    }

    public static void dT(String tag, @NonNull Object... contents) {
        dF(tag, false, contents);
    }

    public static void dF(boolean isWriterFile, @NonNull Object... contents) {
        log(HiLogType.D, isWriterFile, contents);
    }

    public static void dF(String tag, boolean isWriterFile, @NonNull Object... contents) {
        log(HiLogType.D, tag, isWriterFile, contents);
    }

    public static void i(@NonNull Object... contents) {
        iF(false, contents);
    }

    public static void iT(String tag, @NonNull Object... contents) {
        iF(tag, false, contents);
    }

    public static void iF(boolean isWriterFile, @NonNull Object... contents) {
        log(HiLogType.I, isWriterFile, contents);
    }

    public static void iF(String tag, boolean isWriterFile, @NonNull Object... contents) {
        log(HiLogType.I, tag, isWriterFile, contents);
    }

    public static void w(@NonNull Object... contents) {
        wF(false, contents);
    }

    public static void wT(String tag, @NonNull Object... contents) {
        wF(tag, false, contents);
    }

    public static void wF(boolean isWriterFile, @NonNull Object... contents) {
        log(HiLogType.W, isWriterFile, contents);
    }

    public static void wF(String tag, boolean isWriterFile, @NonNull Object... contents) {
        log(HiLogType.W, tag, isWriterFile, contents);
    }

    public static void e(@NonNull Object... contents) {
        eF(false, contents);
    }

    public static void eT(String tag, @NonNull Object... contents) {
        eF(tag, false, contents);
    }

    public static void eF(boolean isWriterFile, @NonNull Object... contents) {
        log(HiLogType.E, isWriterFile, contents);
    }

    public static void eF(String tag, boolean isWriterFile, @NonNull Object... contents) {
        log(HiLogType.E, tag, isWriterFile, contents);
    }

    public static void a(@NonNull Object... contents) {
        aF(false, contents);
    }

    public static void aT(String tag, @NonNull Object... contents) {
        aF(tag, false, contents);
    }

    public static void aF(boolean isWriterFile, @NonNull Object... contents) {
        log(HiLogType.A, isWriterFile, contents);
    }

    public static void aF(String tag, boolean isWriterFile, @NonNull Object... contents) {
        log(HiLogType.A, tag, isWriterFile, contents);
    }

    //endregion

    private static void log(@HiLogType.TYPE int type, boolean isWriterFile, Object... contents) {
        log(type, HiLogManager.getInstance().getConfig().getGlobalTag(), isWriterFile, contents);
    }

    private static void log(@HiLogType.TYPE int type, String tag, boolean isWriterFile, Object... contents) {
        log(HiLogManager.getInstance().getConfig(), type, tag, isWriterFile, contents);
    }

    private static void log(HiLogConfig config, int type, String tag, boolean isWriterFile, Object... contents) {
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
            if (!isWriterFile && hiLogPrinter instanceof HiFilePrinter) {
                continue;
            }
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
