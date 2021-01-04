package com.zj.hi_library.hiLog.format;

/**
 * 转换器接口
 *
 * @param <T>
 */
public interface HiLogFormatter<T> {

    String format(T data);
}
