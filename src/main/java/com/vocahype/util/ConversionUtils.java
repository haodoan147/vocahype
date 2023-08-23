package com.vocahype.util;

public final class ConversionUtils {
    private ConversionUtils() {
    }

    public static int roundDown(int number, int roundTo) {
        return (number / roundTo) * roundTo;
    }
}
