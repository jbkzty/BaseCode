package com.concurrent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * 控制某些资源同时被访问的个数的类
 *
 * @author superKun
 * @date 2018/5/6.
 */
public class SemaphoreDemo {

    public static void main(String[] args) {

        // 线程池
        ExecutorService exec = Executors.newCachedThreadPool();

        // 只能5个线程同时访问
        final Semaphore semaphore = new Semaphore(5);

        semaphore.tryAcquire()

        // 模拟20个客户端访问
        for (int index = 0; index < 50; index++) {
            final int NO = index;
            Runnable run = new Runnable() {
                @Override
                public void run() {
                    try {
                        // 获取许可
                        semaphore.acquire();
                        System.out.println("Accessing: " + NO);
                        Thread.sleep((long) (Math.random() * 6000));
                        // 访问完后，释放
                        semaphore.release();
                        //availablePermits()指的是当前信号灯库中有多少个可以被使用
                        System.out.println("-----------------" + semaphore.availablePermits());
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            };
            exec.execute(run);
        }
        // 退出线程池
        exec.shutdown();
    }
}
