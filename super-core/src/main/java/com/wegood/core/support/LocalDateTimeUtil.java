package com.wegood.core.support;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import lombok.NonNull;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * @author Rain
 */
public class LocalDateTimeUtil {

    /**
     * 默认使用系统当前时区
     */
    public static final ZoneId ZONE = ZoneId.systemDefault();

    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static LocalDateTime dateToLocalDateTime(Date date) {
        if (date == null) {
            return null;
        }
        return LocalDateTime.ofInstant(date.toInstant(), ZONE);
    }

    public static LocalDate dateToLocalDate(@NonNull Date date) {
        return dateToLocalDateTime(date).toLocalDate();
    }

    public static LocalTime dateToLocalTime(@NonNull Date date) {
        return LocalDateTime.ofInstant(date.toInstant(), ZONE).toLocalTime();
    }

    public static Date localDateToDate(@NonNull LocalDate localDate) {
        return Date.from(localDate.atStartOfDay().atZone(ZONE).toInstant());
    }

    public static LocalDateTime localDateToLocalDateTime(@NonNull LocalDate localDate) {
        return localDate.atStartOfDay();
    }

    public static Date localDateTimeToDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZONE).toInstant());
    }

    public static long localDateTimeToEpochMilli(LocalDateTime localDateTime) {
        return localDateTime.atZone(ZONE).toInstant().toEpochMilli();
    }

    public static long localDateToEpochMilli(LocalDate localDate) {
        return localDateTimeToEpochMilli(localDate.atStartOfDay());
    }

    public static Long localTimeToEpochMilli(LocalTime value) {
        return localDateTimeToEpochMilli(value.atDate(EPOCH_LOCAL_DATE));
    }

    public static LocalDateTime epochMilliToLocalDateTime(long epochMilli) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(epochMilli), ZONE);
    }

    public static LocalDate epochMilliToLocalDate(long epochMilli) {
        return epochMilliToLocalDateTime(epochMilli).toLocalDate();
    }

    public static LocalTime epochMilliToLocalTime(long epochMilli) {
        return epochMilliToLocalDateTime(epochMilli).toLocalTime();
    }

    public static long epochMilliToDateEpochMilli(long epochMilli) {
        return localDateToEpochMilli(epochMilliToLocalDate(epochMilli));
    }

    public static LocalDateTime now() {
        return epochMilliToLocalDateTime(SystemClock.now());
    }

    public static LocalTime nowLocalTime() {
        return epochMilliToLocalTime(SystemClock.now());
    }

    public static LocalDate nowLocalDate() {
        return epochMilliToLocalDate(SystemClock.now());
    }

    public static Date nowDate() {
        return new Date(SystemClock.now());
    }

    /**
     * 1970-01-01
     */
    public static LocalDate EPOCH_LOCAL_DATE = epochMilliToLocalDate(0);

    public static LocalDateTime stringToLocalDateTime(String date) {
        if (StrUtil.isBlank(date)) {
            return null;
        }
        return dateToLocalDateTime(DateUtil.parse(date));
    }
}
