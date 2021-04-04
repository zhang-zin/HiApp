package com.zj.hiapp.test;

import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 演示生产者与消费者的场景， 利用ReenactmentLock condition条件对象。能够指定唤醒某个线程去工作
 * <p>
 * 生产者是 一个boss 生产砖，砖的序列号为偶数让工人2去搬，奇数号工人1去搬
 * <p>
 * 消费者是工人
 */
public class ReentrantLockDemo2 {

    static class ReentrantLockTask {

        ReentrantLock reentrantLock = new ReentrantLock();
        private Condition worker1Condition;
        private Condition worker2Condition;

        volatile int flag = 0; //砖的序列号

        public ReentrantLockTask() {
            worker1Condition = reentrantLock.newCondition();
            worker2Condition = reentrantLock.newCondition();
        }

        //工人1搬砖
        void worker1() {
            reentrantLock.lock();
            try {
                if (flag == 0 || flag % 2 != 0) {
                    System.out.println("worker1 无砖可搬");
                    worker1Condition.await();
                }
                System.out.println("worker1 搬到的砖是：" + flag);
                flag = 0;
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                reentrantLock.unlock();
            }
        }

        //工人1搬砖
        void worker2() {
            reentrantLock.lock();
            try {
                if (flag == 0 || flag % 2 == 0) {
                    System.out.println("worker2 无砖可搬");
                    worker2Condition.await();
                }
                System.out.println("worker2 搬到的砖是：" + flag);
                flag = 0;
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                reentrantLock.unlock();
            }
        }

        //boss生产砖
        void boss() {
            reentrantLock.lock();
            try {
                flag = new Random().nextInt(100);
                if (flag % 2 == 0) {
                    worker2Condition.signal();
                    System.out.println("生产的砖是：唤醒工人2" + flag);
                } else {
                    worker1Condition.signal();
                    System.out.println("生产的砖是：唤醒工人1" + flag);
                }
            } finally {
                reentrantLock.unlock();
            }
        }
    }
}
