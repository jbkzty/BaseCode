package com.thread.future.v1;

import com.thread.future.ExchangeService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.stream.Collectors;

/**
 * @author spuerKun
 * @date 17/12/31.
 */
public class BestPriceFinder {

    private final List<Shop> shops = Arrays.asList(
            new Shop("BestPrice"),
            new Shop("LetsSaveBig"),
            new Shop("MyFavoriteShop"),
            new Shop("BuyItAll"));

    private final Executor executor = Executors.newFixedThreadPool(shops.size(), new ThreadFactory() {
        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(r);
            t.setDaemon(true);
            return t;
        }
    });

    public List<String> findPricesSequential(String product) {
        return shops.stream().map(shop ->
                shop.getName() + " price is " + shop.getPrice(product))
                .collect(Collectors.toList());
    }

    public List<String> findPricesParallel(String product) {
        return shops.parallelStream().map(shop ->
                shop.getName() + " price is " + shop.getPrice(product))
                .collect(Collectors.toList());
    }

    public List<String> findPricesFuture(String product) {

        List<CompletableFuture<String>> priceFutures =
                shops.stream().map(shop -> CompletableFuture.supplyAsync(
                        () -> shop.getName() + " price is " + shop.getPrice(product), executor
                )).collect(Collectors.toList());

        List<String> prices = priceFutures.stream()
                .map(CompletableFuture::join)
                .collect(Collectors.toList());

        return prices;
    }

    public List<String> findPriceInUSD(String product) {

        List<CompletableFuture<Double>> priceFutures = new ArrayList<>();

//        for (Shop shop : shops) {
//
//            CompletableFuture<Double> futurePriceInUSD = CompletableFuture.supplyAsync(() -> shop.getPrice(product)).thenCombine(
//                    CompletableFuture.supplyAsync(
//                            () -> ExchangeService.getRate(ExchangeService.Money.EUR, ExchangeService.Money.USD),
//                            (price, rate) -> price * rate)
//            );
//
//        }
        return null;
    }

    public List<String> findPriceInUSDJava7(String product) {
        ExecutorService executor = Executors.newCachedThreadPool();
        List<Future<Double>> priceFutures = new ArrayList<>();

        for (Shop shop : shops) {
            final Future<Double> futureRate = executor.submit(new Callable<Double>() {
                @Override
                public Double call() {
                    return ExchangeService.getRate(ExchangeService.Money.EUR, ExchangeService.Money.USD);
                }
            });

            Future<Double> futurePriceInUSD = executor.submit(new Callable<Double>() {
                @Override
                public Double call() throws Exception {
                    try {
                        double priceInEUR = shop.getPrice(product);
                        return priceInEUR * futureRate.get();
                    } catch (InterruptedException | ExecutionException e) {
                        throw new RuntimeException(e.getMessage(), e);
                    }
                }
            });
            priceFutures.add(futurePriceInUSD);
        }

        List<String> prices = new ArrayList<>();
        for (Future<Double> priceFuture : priceFutures) {
            try {
                prices.add(" price is " + priceFuture.get());
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }
        return prices;
    }
}
