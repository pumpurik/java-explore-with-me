package ru.practicum.ewm.urils;

import java.util.concurrent.Callable;
import java.util.function.BiFunction;

public class NullUtils {
    public static BiFunction<String, String, Boolean> equalsIgnoreNullFalse = (f, s) -> {
        if (f == null || s == null) return false;
        else return f.equals(s);
    };
    public static BiFunction<String, String, Boolean> equalsIgnoreNullTrue = (f, s) -> {
        if (f == null || s == null) return true;
        else return f.equals(s);
    };

    public static <T> T getOrDefault(Callable<T> callable, T defaultValue) {
        try {
            return callable.call();
        } catch (Throwable e) {
            return defaultValue;
        }
    }

    public static <T> T getOrNull(Callable<T> callable) {
        return getOrDefault(callable, null);
    }
}
