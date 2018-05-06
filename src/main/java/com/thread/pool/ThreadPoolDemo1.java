package com.thread.pool;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author spuerKun
 * @date 18/1/15.
 */
public class ThreadPoolDemo1 {

    private static BlockingQueue blockingQueue = new ArrayBlockingQueue<>(10);

//    final ThreadFactory threadFactory = new ThreadFactoryBuilder()
//            .setNameFormat("Orders-%d")
//            .setDaemon(true)
//            .build();
//
//    private static final ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
//            10,
//            20,
//            1,
//            TimeUnit.MINUTES,
//            blockingQueue, threadFactory, new ThreadPoolExecutor.AbortPolicy());

    private static final int COUNT_BITS = Integer.SIZE - 3;
    private static final int CAPACITY   = (1 << COUNT_BITS) - 1;
    private static int ctlOf(int rs, int wc) { return rs | wc; }
    private static final AtomicInteger ctl = new AtomicInteger(ctlOf(-1 << COUNT_BITS, 0));

    // runState is stored in the high-order bits
    private static final int RUNNING    = -1 << COUNT_BITS;

    public static void main(String[] args) {
        int c = ctl.get();
        System.out.println(c);
//        threadPoolExecutor.submit(() -> {
//            System.out.println("---");
//        });

    }

}
