package com.zj.hiapp.test;

import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLockDemo {

    static class ReentrantLockTask {

        ReentrantLock reentrantLock = new ReentrantLock();
        void buyTicket() {
            String name = Thread.currentThread().getName();
            System.out.println(name);
            try {
                reentrantLock.lock();
                System.out.println(name + "准备好了");
                Thread.sleep(1000);
                System.out.println(name + "买好了");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                reentrantLock.unlock();
            }
        }
    }
}
