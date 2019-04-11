package io.renren.common.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class ThreadPoolUtils {

    private static final int POOL_SIZE = Runtime.getRuntime().availableProcessors() * 4;

    private static ExecutorService exec = Executors.newFixedThreadPool(POOL_SIZE);

    private ThreadPoolUtils() {
    }

    public static void execute(Runnable runnable) {
        exec.execute(runnable);
    }
}
