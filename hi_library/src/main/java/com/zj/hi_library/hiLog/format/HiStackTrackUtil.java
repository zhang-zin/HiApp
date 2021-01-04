package com.zj.hi_library.hiLog.format;

import android.text.TextUtils;

/**
 * 堆栈工具类
 * 获取不包含com.zj.hi_library.hiLog下的堆栈和HiLogConfig配置的堆栈深度
 */
public class HiStackTrackUtil {

    public static StackTraceElement[] getCroppedRealStackTrack(StackTraceElement[] stackTrace, String ignorePackage, int maxDepth) {
        return cropStackTrace(getRealStackTrack(stackTrace, ignorePackage), maxDepth);
    }

    /**
     * 排除忽略的包名
     *
     * @param stackTraceElements 原始堆栈信息
     * @param ignorePackage      忽略的包名
     */
    private static StackTraceElement[] getRealStackTrack(StackTraceElement[] stackTraceElements, String ignorePackage) {
        int ignoreDepth = 0;
        int allDepth = stackTraceElements.length;
        for (int i = stackTraceElements.length - 1; i >= 0; i--) {
            String className = stackTraceElements[i].getClassName();
            if (!TextUtils.isEmpty(ignorePackage) && className.startsWith(ignorePackage)) {
                ignoreDepth = i + 1;
                break;
            }
        }
        int realDepth = allDepth - ignoreDepth;
        StackTraceElement[] realStack = new StackTraceElement[realDepth];
        System.arraycopy(stackTraceElements, ignoreDepth, realStack, 0, realDepth);
        return realStack;
    }

    /**
     * 获取最大深度的堆栈信息
     *
     * @param stackTraceElements 原始堆栈信息
     * @param maxDepth           最大深度
     */
    private static StackTraceElement[] cropStackTrace(StackTraceElement[] stackTraceElements, int maxDepth) {
        int realDepth = stackTraceElements.length;
        if (maxDepth > 0) {
            realDepth = Math.min(realDepth, maxDepth);
        }
        StackTraceElement[] copy = new StackTraceElement[realDepth];
        System.arraycopy(stackTraceElements, 0, copy, 0, realDepth);
        return copy;
    }

}
