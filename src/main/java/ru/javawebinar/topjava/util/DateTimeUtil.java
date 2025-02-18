package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class DateTimeUtil {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static boolean isBetweenHalfOpen(LocalTime lt, LocalTime startTime, LocalTime endTime) {
        return !lt.isBefore(startTime) && lt.isBefore(endTime);
    }

    public static List<Meal> filterByDateAndTime(List<Meal> meals,
                                                 LocalDate startDate, LocalDate endDate,
                                                 LocalTime startTime, LocalTime endTime) {
        if (startDate != null || endDate != null) {
            meals = meals.stream()
                    .filter(meal -> (startDate == null || !meal.getDate().isBefore(startDate)) &&
                            (endDate == null || !meal.getDate().isAfter(endDate)))
                    .collect(Collectors.toList());
        }

        if (startTime != null || endTime != null) {
            meals = meals.stream()
                    .filter(meal ->
                            (startTime == null || !meal.getTime().isBefore(startTime)) &&
                                    (endTime == null || meal.getTime().isBefore(endTime)))
                    .collect(Collectors.toList());
        }
        return meals;
    }

    public static String toString(LocalDateTime ldt) {
        return ldt == null ? "" : ldt.format(DATE_TIME_FORMATTER);
    }
}
