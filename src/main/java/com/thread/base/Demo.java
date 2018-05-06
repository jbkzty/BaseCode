package com.thread.base;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @author spuerKun
 * @date 18/1/6.
 */
public class Demo {
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newCachedThreadPool();
        Callable<String> callable = new Callable<String>() {
            @Override
            public String call() throws Exception {
                int a = 1 / 0;
                return "aaa";
            }
        };
        Future<String> future = executorService.submit(callable);
        try {
            String aa = future.get();
            System.out.println(aa);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

}
