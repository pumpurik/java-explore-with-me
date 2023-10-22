package ru.practicum.ewm.stats.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtils {
    public static LocalDateTime stringToLocalDateTimeWithPattern(String date, String pattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return LocalDateTime.parse(date, formatter);
    }
}
