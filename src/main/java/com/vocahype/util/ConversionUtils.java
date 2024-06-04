package com.vocahype.util;

public final class ConversionUtils {
    private ConversionUtils() {
    }

    public static int roundDown(int number, int roundTo) {
        return (number / roundTo) * roundTo;
    }

    public static long roundUp(long dividend, int divisor) {
        long result = dividend / divisor;
        if (dividend % divisor != 0) {
            result++;
        }
        return result;
    }
}
