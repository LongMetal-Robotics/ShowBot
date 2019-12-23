package org.longmetal.util;

public class Math {
    public static Number limit(Number value, Number min, Number max) {
        if (value.doubleValue() < min.doubleValue()) {
            value = min;
        } else if (value.doubleValue() > max.doubleValue()) {
            value = max;
        }
        return value;
    }
}