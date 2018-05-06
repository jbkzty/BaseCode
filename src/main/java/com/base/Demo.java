package com.base;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author spuerKun
 * @date 17/12/13.
 */
public class Demo {

    public static class ThreadDemo implements Runnable {
        @Override
        public void run() {
            System.out.println("Id = " + Thread.currentThread().getId());
        }
    }

    public static void main(String[] args) {
        ThreadPoolExecutor THREAD_EXECUTOR = new ThreadPoolExecutor(20, 100, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>());
        for (int i = 0; i < 30; i++) {
            THREAD_EXECUTOR.submit(new ThreadDemo());
        }
    }

}
