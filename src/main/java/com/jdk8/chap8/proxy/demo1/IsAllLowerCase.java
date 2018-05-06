package com.jdk8.chap8.proxy.demo1;

/**
 * @author spuerKun
 * @date 18/1/5.
 */
public class IsAllLowerCase implements ValidatioStrategy {

    @Override
    public boolean execute(String str) {
        return str.matches("[a-z]+");
    }
}
