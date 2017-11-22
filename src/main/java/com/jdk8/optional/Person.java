package com.jdk8.optional;

import java.util.Optional;

/**
 * 一个人可能有车，也可能没有车，因此将这个字段声明为Optional
 *
 * @author spuerKun
 * @date 17/11/20.
 */
public class Person {

    private Optional<Car> car;

    public Optional<Car> getCar() {
        return car;
    }

}
