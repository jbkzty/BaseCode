package com.generics.ginterface;

/**
 * 泛型接口
 *
 * @author spuerKun
 * @date 17/11/10.
 */
public interface Handler<T extends Person> {

    void doSomeThing(T value);
}
