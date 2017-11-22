package com.jdk8.optional;

/**
 * 保险公司必须是有名字的，所以声明为String
 *
 * @author spuerKun
 * @date 17/11/20.
 */
public class Insurance {

    private String name;

    public String getName() {
        return name;
    }

    public Insurance(String name) {
        this.name = name;
    }

}
