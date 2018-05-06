package com.hystrix.demo;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;

/**
 * @author spuerKun
 * @date 17/12/11.
 */
public class HelloWorldCommand extends HystrixCommand<String> {

    protected HelloWorldCommand(String name) {
        super(HystrixCommand.Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey(name)));
    }

    @Override
    protected String run() throws Exception {
        System.out.println("run success " + Thread.currentThread().getName());
        return "run success " + Thread.currentThread().getName();
    }

    public static void main(String[] args) {
        HelloWorldCommand helloWorldCommand = new HelloWorldCommand("klov");
        String result = helloWorldCommand.execute();
        System.out.println("【HelloWorldCommand】 result = " + result);
    }
}
