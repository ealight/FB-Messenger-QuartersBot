package com.bailbots.fb.quartersbot.utils;

import lombok.SneakyThrows;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.IntStream.range;

public class CalendarUtil {
    public static String formatDate(Date date) {
        return new SimpleDateFormat("dd.MM.yyyy").format(date);
    }

    public static Date getDate(Integer year, Integer month, Integer day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        return calendar.getTime();
    }

    public static Date getCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        return calendar.getTime();
    }

    public static Integer getCurrentDay() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.DATE);
    }

    public static Integer getCurrentYear() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.YEAR);
    }

    public static Calendar getCalendarDate(Integer year, Integer month, Integer day) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, month);
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        return calendar;
    }
}
