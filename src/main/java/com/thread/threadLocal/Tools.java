package com.thread.threadLocal;

import java.util.Date;

/**
 * @author spuerKun
 * @date 18/1/13.
 */
public class Tools {

    public static ThreadLocal t1 = new ThreadLocal();

    public static ThreadLocal<Date> t2 = new ThreadLocal<>();

}
