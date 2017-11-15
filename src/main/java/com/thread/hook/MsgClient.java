package com.thread.hook;

import sun.misc.Signal;
import sun.misc.SignalHandler;

import java.util.concurrent.*;

/**
 * (1) linux中，进程的关闭是靠信号量实现的
 * <p>
 * (2) 方法一:
 * 程序中添加Runtime.getRuntime().addShutdownHook钩子方法，SIGTERM,SIGINT,SIGHUP三种信号都会触发该方法（分别对应kill -1/kill -2/kill -15，Ctrl+C也会触发SIGINT信号）
 * <p>
 * (3) 方法二:
 * 程序中通过Signal类注册信号监听，比如USR2（对应kill -12），在handle方法中执行关闭操作
 *
 * @author spuerKun
 * @date 17/11/12.
 */
public class MsgClient {

    /**
     * 模拟消费线程池，同时4个线程处理
     */
    private static final ThreadPoolExecutor THREAD_POOL = (ThreadPoolExecutor) Executors.newFixedThreadPool(4);

    /**
     * 用于模拟消息生产任务
     */
    private static final ScheduledExecutorService SCHEDULED_EXECUTOR_SERVICE = Executors.newSingleThreadScheduledExecutor();

    /**
     * 用于判断是否关闭订阅
     */
    private static volatile boolean isClose = false;

    public static void main(String[] args) throws InterruptedException {
        // 注册钩子
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                close();
            }
        });

        BlockingQueue<String> queue = new ArrayBlockingQueue<String>(100);
        producer(queue);
        consumer(queue);
    }

    static {
        String osName = System.getProperty("os.name").toLowerCase();
        if (osName != null && osName.indexOf("window") == -1) {
            // 注册linux kill 信号量 kill -12
            Signal signal = new Signal("USR2");
            Signal.handle(signal, new SignalHandler() {
                @Override
                public void handle(Signal signal) {
                    close();
                }
            });
        }
    }

    /**
     * 模拟消息队列生产者
     */
    private static void producer(final BlockingQueue queue) {
        // 每200毫秒向队列中放入一个消息
        SCHEDULED_EXECUTOR_SERVICE.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                queue.offer("");
            }
        }, 0L, 200L, TimeUnit.MILLISECONDS);
    }

    /**
     * 模拟消息队列消费者 生产者每秒生产5个   消费者4个线程消费1个1秒  每秒积压1个
     */
    private static void consumer(final BlockingQueue<String> queue) throws InterruptedException {
        while (!isClose) {
            // 获取线程池还未处理的消息
            getPoolBacklogSize();
            // 从队列中拿消息
            final String msg = queue.take();
            // 放入线程池处理
            if (!THREAD_POOL.isShutdown()) {
                THREAD_POOL.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            TimeUnit.MILLISECONDS.sleep(1000L);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

        }
    }

    /**
     * 查看线程池推积消息的个数
     *
     * @return
     */
    private static long getPoolBacklogSize() {
        long backlog = THREAD_POOL.getTaskCount() - THREAD_POOL.getCompletedTaskCount();
        System.out.println(String.format("[%s]THREAD_POOL backlog:%s", System.currentTimeMillis(), backlog));
        return backlog;
    }

    private static void close() {
        System.out.println("收到kill消息，执行关闭操作");
        // 关闭订阅消费
        isClose = true;
        // 关闭线程池，等待线程池积压消息处理
        THREAD_POOL.shutdown();

        //判断线程池是否关闭
        while (!THREAD_POOL.isTerminated()) {
            try {
                //每200毫秒 判断线程池积压数量
                getPoolBacklogSize();
                TimeUnit.MILLISECONDS.sleep(200L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("订阅者关闭，线程池处理完毕");
    }
}
