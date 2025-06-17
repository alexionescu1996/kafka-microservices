package com.example.order;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.*;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class CompleteableFutureTest {

    @Test
    void test()
            throws InterruptedException, ExecutionException {

        try (var svc = Executors.newFixedThreadPool(2)) {
            svc.submit(() -> System.out.println("hello W"));
            Runnable print = () -> System.out.println("Hello_print");
            Runnable newLine = System.out::println;

            svc.execute(newLine);
            svc.execute(print);

            Callable<String> print3 = () -> "Hello3";
            Future<String> futureResult = svc.submit(print3);
            String result = futureResult.get();
            System.out.println(result);
        }
    }


    @Test
    void test2()
            throws InterruptedException, ExecutionException {

        try (var svc = Executors.newFixedThreadPool(2)) {

            Callable<String> printDelay = () -> {
                Thread.sleep(3_500);
                System.out.println("printing from " + Thread.currentThread().getName());
                return "Print Delayed";
            };

            Future<String> future = svc.submit(printDelay);

            String result = future.get();
            System.out.println("printing from " + Thread.currentThread().getName());

            System.out.println(result);
        }
    }

    @Test
    void test3()
            throws InterruptedException, ExecutionException, TimeoutException {

        try (var svc = Executors.newFixedThreadPool(2)) {

            Callable<String> callable = () -> {
                Thread.sleep(3_500);
                System.out.println("printing from " + Thread.currentThread().getName());
                return "Print Delayed";
            };

            Future<String> future = svc.submit(callable);

            System.out.println("printing from " + Thread.currentThread().getName());

            assertThrows(TimeoutException.class,
                    () -> future.get(1_50, TimeUnit.MILLISECONDS));


        }
    }

    @Test
    void test4()
            throws InterruptedException, ExecutionException {

        try (var svc = Executors.newFixedThreadPool(2)) {

            CompletableFuture<String> completableFuture = new CompletableFuture<>();

            svc.submit(() -> {
                Thread.sleep(3_500);
                completableFuture.complete("Print Delayed");
                return null;
            });

            String result = completableFuture.get();
            System.out.println(result);
        }
    }

    @Test
    void test5()
            throws InterruptedException, ExecutionException {

        try (var svc = Executors.newFixedThreadPool(2)) {
            CompletableFuture<String> completableFuture = new CompletableFuture<>();

            Callable<Void> callable = () -> {
                Thread.sleep(3_500);
                completableFuture.complete("Print Delayed");
                System.out.println("printing from " + Thread.currentThread().getName());
                return null;
            };

            svc.submit(callable);
            String result = completableFuture.get();
            System.out.println("printing from " + Thread.currentThread().getName());

            System.out.println(result);
        }
    }

    @Test
    void test6()
            throws InterruptedException, ExecutionException {
//
        CompletableFuture[] futures = Stream.generate(this::getInt)
                .limit(10)
                .toArray(CompletableFuture[]::new);

        CompletableFuture
                .allOf(futures)
                .join();

        Arrays.stream(futures)
                .map(CompletableFuture::join)
                .forEach(System.out::println);
    }

    @Test
    void test7() {

        CompletableFuture<Integer> getInt = CompletableFuture
                .supplyAsync(() -> new Random().nextInt(300))
                .thenApply((future) -> future * -100);

        CompletableFuture<String> getStrings = CompletableFuture
                .supplyAsync(() -> UUID.randomUUID().toString());

        CompletableFuture.allOf(getStrings, getInt)
                .join();

        CompletableFuture.allOf(getStrings, getInt)
                .thenRun(() -> {
                    Integer x = getInt.join();
                    String y = getStrings.join();
                    System.out.println(x + " " + y);
                });
    }

    @Test
    void test8() {

        CompletableFuture<Integer> getInt = CompletableFuture
                .supplyAsync(() -> new Random().nextInt(300))
                .thenApply((future) -> future * -100);

        CompletableFuture<String> getStrings = CompletableFuture
                .supplyAsync(() -> UUID.randomUUID().toString())
                .thenApply((result) -> result.replaceFirst("-", "@"));

        getStrings.thenCombine(getInt, (r1, r2) -> r1 + r2);

        Stream.of(getStrings, getInt)
                .map(CompletableFuture::join)
                .forEach(System.out::println);

    }

    @Test
    void test9() {
        CompletableFuture<String> getString = this.getString(1_500);

        CompletableFuture<String> getInt = CompletableFuture.supplyAsync(() -> {
            try {
                System.out.println(Thread.currentThread().getName() + " Execution thread");
                Thread.sleep(2_000);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return String.valueOf(new Random().nextInt());
        });

        getInt.acceptEither(
                getString,
                future -> System.out.println("first completed: " + future)
        ).join();

    }

    CompletableFuture<Integer> getInt() {
        return CompletableFuture.supplyAsync(() -> new Random().nextInt());
    }

    CompletableFuture<String> getString(long milis) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                System.out.println(Thread.currentThread().getName() + " Execution thread");
                Thread.sleep(milis);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return UUID.randomUUID().toString();
        });
    }

}



