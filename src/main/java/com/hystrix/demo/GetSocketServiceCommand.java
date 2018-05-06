package com.hystrix.demo;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixCommandProperties;
import com.netflix.hystrix.HystrixThreadPoolKey;
import com.netflix.hystrix.HystrixThreadPoolProperties;

/**
 * @author spuerKun
 * @date 17/12/11.
 */
public class GetSocketServiceCommand extends HystrixCommand<String> {

    protected GetSocketServiceCommand(HystrixCommandGroupKey group) {
        super(setter());
    }

    private static Setter setter() {

        // 服务分组
        // 配置全局唯一标识服务分组的名称，比如库存系统就是一个服务分组,当我们监控的时候，相同的分组就会聚合在一起
        HystrixCommandGroupKey groupKey = HystrixCommandGroupKey.Factory.asKey("stock");

        // 服务标识
        // 配置全局唯一标识服务名称,比如库存系统中有一个获取库存服务,如果不配置，则默认为简单类名
        HystrixCommandKey commandKey = HystrixCommandKey.Factory.asKey("getStock");

        // 线程池名称
        // 配置全局唯一标识线程池名称
        HystrixThreadPoolKey threadPoolKey = HystrixThreadPoolKey.Factory.asKey("stock-pool");

        // 线程池配置
        // 配置线程池参数
        HystrixThreadPoolProperties.Setter threadPoolProperties = HystrixThreadPoolProperties.Setter()
                .withCoreSize(10)
                .withKeepAliveTimeMinutes(5)
                .withMaxQueueSize(Integer.MAX_VALUE)
                .withQueueSizeRejectionThreshold(1000);

        // 命令属性配置
        // 配置该命令的一些参数,这边配置的是线程池隔离
        HystrixCommandProperties.Setter commandProperties = HystrixCommandProperties.Setter()
                .withExecutionIsolationStrategy(HystrixCommandProperties.ExecutionIsolationStrategy.THREAD);

        /**
         *    也可以使用信号量隔离
         *    HystrixCommandProperties.Setter commandProperties2 = HystrixCommandProperties.Setter()
         *                 .withExecutionIsolationStrategy(HystrixCommandProperties.ExecutionIsolationStrategy.SEMAPHORE)
         *                 .withExecutionIsolationSemaphoreMaxConcurrentRequests(50);
         *
         *    信号隔离只是限制了总的并发数，服务使用主线程进行同步调用，即没有线程池，因此，如果只是想限制某个服务总的并发调用量或者调用的服务不涉及远程调用的话，
         * 可以使用轻量级的信号量来实现
         */

        return HystrixCommand.Setter
                .withGroupKey(groupKey)
                .andCommandKey(commandKey)
                .andThreadPoolKey(threadPoolKey)
                .andThreadPoolPropertiesDefaults(threadPoolProperties)
                .andCommandPropertiesDefaults(commandProperties);
    }

    @Override
    protected String run() throws Exception {
        return "get socket message";
    }
}
