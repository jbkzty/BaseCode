package com.thread.base;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * @author spuerKun
 * @date 17/12/28.
 */
public class FutureTaskDemo {

    public static void main(String[] args) {
        CallAbleThread thread = new CallAbleThread();

        FutureTask<String> futureTask = new FutureTask<>(thread);

        Thread mainThread = new Thread(futureTask);
        mainThread.start();

        do {
        } while (!futureTask.isDone());

        try {
            String result = futureTask.get();
            System.out.println(result);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    static class CallAbleThread implements Callable<String> {
        @Override
        public String call() throws Exception {
            Thread.sleep(1000);
            return "Thread-Name:" + Thread.currentThread().getName();
        }
    }
}
