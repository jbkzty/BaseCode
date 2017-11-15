package com.base;

/**
 * @author spuerKun
 * @date 17/11/10.
 */
public interface Handler<T extends Person> {

    void doSomeThing(T value);
}
