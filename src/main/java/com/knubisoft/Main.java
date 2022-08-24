package com.knubisoft;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
    private static int number;
    private static final AtomicInteger atomic = new AtomicInteger(0);
    private static final int CYCLE = 10_000;
    private static final int SLEEPING = 10_000;
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public Main() {
    }

    @SneakyThrows
    public static void main(String[] args) {
        byCycleFor();
        byStreamApi();
        byAtomic();
        bySync();
    }

    @SneakyThrows
    private static void byCycleFor() {
        number = 0;
        List<Thread> threadListFOR = IntStream.range(0, 4)
                .boxed()
                .map(num -> new Thread(() -> {
                    for (int i = 0; i < CYCLE; i++) {
                        add();
                    }
                }))
                .collect(Collectors.toList());

        threadListFOR.forEach(Thread::start);
        Thread.sleep(SLEEPING);
        logger.info("By cycle FOR : " + number);
    }

    @SneakyThrows
    private static void byStreamApi() {
        number = 0;
        List<Thread> threadListStreamApi = IntStream.range(0, 4)
                .boxed()
                .map(num -> new Thread(() -> IntStream.range(0, CYCLE)
                        .boxed()
                        .forEach(numCycle -> add())))
                .collect(Collectors.toList());

        threadListStreamApi.forEach(Thread::start);
        Thread.sleep(SLEEPING);
        logger.info("By cycle STREAM API : " + number);
    }

    @SneakyThrows
    private static void byAtomic() {
        List<Thread> threadListAtomic = IntStream.range(0, 4)
                .boxed()
                .map(num -> new Thread(() -> {
                    for (int i = 0; i < CYCLE; i++) {
                        addToAtomic();
                    }
                }))
                .collect(Collectors.toList());

        threadListAtomic.forEach(Thread::start);
        Thread.sleep(SLEEPING);
        logger.info("By Atomic : " + atomic);
    }

    @SneakyThrows
    private static void bySync() {
        number = 0;
        List<Thread> threadListSync = IntStream.range(0, 4)
                .boxed()
                .map(num -> new Thread(() -> {
                    for (int i = 0; i < CYCLE; i++) {
                        addSync();
                    }
                }))
                .collect(Collectors.toList());

        threadListSync.forEach(Thread::start);
        Thread.sleep(SLEEPING);
        logger.info("By SYNC: " + number);
    }

    private static void add() {
        number++;
    }

    private static synchronized void addSync() {
        number++;
    }

    private static void addToAtomic() {
        atomic.addAndGet(1);
    }
}
