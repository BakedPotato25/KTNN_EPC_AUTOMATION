package com.ktnn.utils;

import java.time.Instant;
import java.time.format.DateTimeFormatter;

public class DateTimeUtils {
    public static long getCurrentTimestamp() {
        return Instant.now().toEpochMilli();
    }

    // Lấy thời gian hiện tại theo format "yyyyMMdd_HHmmsss"
    public static String getCurrentTimeFormatted() {
        return DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")
                .format(java.time.LocalDateTime.now());
    }
}
