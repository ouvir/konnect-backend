package com.konnect.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtils {
    private static final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static String getNowDateString() {
        return LocalDateTime.now().format(dateFormat);
    }

    public static String getDateString(LocalDateTime dateTime) {
        return dateTime.format(dateFormat);
    }
}