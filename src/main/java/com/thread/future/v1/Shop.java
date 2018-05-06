package com.thread.future.v1;

import com.thread.future.Utils;

import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

/**
 * @author spuerKun
 * @date 17/12/31.
 */
public class Shop {

    private final String name;
    private final Random random;

    public Shop(String name) {
        this.name = name;
        random = new Random(name.charAt(0) * name.charAt(1) * name.charAt(2));
    }

    public double getPrice(String product) {
        return calculatePrice(product);
    }

    public String getName() {
        return name;
    }

    /**
     * 模拟同步的请求，延迟1秒
     *
     * @param product
     * @return
     */
    public double calculatePrice(String product) {
        Utils.delay();
        return random.nextDouble() * product.charAt(0) + product.charAt(1);
    }

    /**
     * (1) 创建CompletableFuture对象，它会包含计算的结果
     * (2) 在另外一个线程中以异步的方式执行计算
     * (3) 无需等待还没结束的计算，直接返回Future对象
     *
     * @param product
     * @return
     */
    public Future<Double> getPriceAsync(String product) {

        CompletableFuture<Double> futurePrice = new CompletableFuture<>();

        new Thread(() -> {
            double price = calculatePrice(product);
            futurePrice.complete(price);
        }).start();

        return futurePrice;
    }


}
