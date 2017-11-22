package com.generics.ginterface;

/**
 * @author spuerKun
 * @date 17/11/10.
 */
public class HandlerImpl implements Handler<Man> {

    @Override
    public void doSomeThing(Man value) {
        System.out.println(value.getName());

    }
}
