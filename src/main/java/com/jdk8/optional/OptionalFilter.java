package com.jdk8.optional;

import java.util.Optional;

/**
 * @author spuerKun
 * @date 17/11/23.
 */
public class OptionalFilter {

    public static void main(String[] args) {
        Optional<Insurance> optInsurance = Optional.ofNullable(new Insurance("jbk"));
        optInsurance.filter(insurance -> "jbk".equals(insurance.getName()))
                .ifPresent(x -> System.out.println("ok"));
    }
}
