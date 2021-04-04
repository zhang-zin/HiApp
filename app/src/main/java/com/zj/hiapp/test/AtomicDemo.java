package com.zj.hiapp.test;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 一个用原子类修饰，一个用volatile修饰，在多线程的情况下做自增，然后输出最后的值
 *
 * @author 张锦
 */
public class AtomicDemo {

    static class AtomicTask {

        AtomicInteger atomicInteger = new AtomicInteger();

        volatile int volatileCount = 0;

        void incrementAtomic() {

            atomicInteger.getAndIncrement();
        }

        void incrementVolatile() {
            volatileCount++;
        }
    }
}
