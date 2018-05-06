package com.thread.future;

/**
 * @author spuerKun
 * @date 17/12/31.
 */
public class Utils {

    public static void delay() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
