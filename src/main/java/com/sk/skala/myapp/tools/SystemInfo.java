package com.sk.skala.myapp.tools;

public class SystemInfo {

    public static int getAvailableProcessors() {
        return Runtime.getRuntime().availableProcessors();
    }

    public static long getTotalMemory() {
        return Runtime.getRuntime().totalMemory();
    }

    public static long getFreeMemory() {
        return Runtime.getRuntime().freeMemory();
    }

    public static long getMaxMemory() {
        return Runtime.getRuntime().maxMemory();
    }

    public static String getMemoryUsage() {
        return String.format("사용 가능 프로세서: %d, 총 메모리: %dMB, 사용 가능 메모리: %dMB, 최대 메모리: %dMB",
                getAvailableProcessors(),
                getTotalMemory() / (1024 * 1024),
                getFreeMemory() / (1024 * 1024),
                getMaxMemory() / (1024 * 1024));
    }
}
