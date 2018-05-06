package com.thread.threadLocal;

import java.util.Date;

/**
 * @author spuerKun
 * @date 18/1/13.
 */
public class ThreadB extends Thread {

    @Override
    public void run() {
        try {
            for (int i = 0; i < 20; i++) {
                if (Tools.t2.get() == null) {
                    Tools.t2.set(new Date());
                }
                System.out.println("B " + Tools.t2.get().getTime());
                Thread.sleep(100);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
