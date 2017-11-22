package com.jdk8.optional;

import java.util.Optional;

/**
 * 车可能进行了保险，也可能没有保险，所以将这个字段声明为Optional
 *
 * @author spuerKun
 * @date 17/11/20.
 */
public class Car {

    private Optional<Insurance> insurance;

    public Optional<Insurance> getInsurance() {
        return insurance;
    }
}
