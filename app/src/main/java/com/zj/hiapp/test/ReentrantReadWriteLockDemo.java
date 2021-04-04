package com.zj.hiapp.test;

import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ReentrantReadWriteLockDemo {

    static class ReentrantReadWriteLockTask {

        private final ReentrantReadWriteLock.ReadLock readLock;
        private final ReentrantReadWriteLock.WriteLock writeLock;
        ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();

        public ReentrantReadWriteLockTask() {
            readLock = readWriteLock.readLock();
            writeLock = readWriteLock.writeLock();
        }

        void read() {
            String name = Thread.currentThread().getName();
            readLock.lock();
            try {
                System.out.println("线程：" + name + "正在读取数据...");
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                readLock.unlock();
                System.out.println("线程：" + name + "释放了锁...");
            }
        }

        void write() {
            String name = Thread.currentThread().getName();
            writeLock.lock();
            try {
                System.out.println("线程：" + name + "正在写入数据...");
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                writeLock.unlock();
                System.out.println("线程：" + name + "释放了锁...");
            }
        }
    }
}
