package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

import static ru.javawebinar.topjava.util.TimeUtil.isBetweenHalfOpen;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> meals = Arrays.asList(
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        );

        List<UserMealWithExcess> mealsTo = filteredByCycles(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        mealsTo.forEach(System.out::println);

        System.out.println();
        filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000).forEach(System.out::println);
    }

    /**
     * Фильтрует список приемов пищи пользователя по заданному временному диапазону и определяет,
     * превышена ли общая суточная норма калорий. Для каждого приема пищи, попадающего в указанный
     * временной интервал, создается объект `UserMealWithExcess`, указывающий, превышена ли суточная
     * норма калорий для даты этого приема пищи.
     *
     * @param meals          список объектов {@code UserMeal}, которые нужно отфильтровать
     * @param startTime      начало временного диапазона (включительно)
     * @param endTime        конец временного диапазона (исключительно)
     * @param caloriesPerDay максимально допустимое количество калорий в день
     * @return список объектов {@code UserMealWithExcess}, представляющих приемы пищи в заданном
     * временном интервале с указанием, превышена ли суточная норма калорий
     */
    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> caloriesByDateMap = new HashMap<>();
        for (UserMeal meal : meals) {
            caloriesByDateMap.merge(meal.getDateTime().toLocalDate(), meal.getCalories(), Integer::sum);
        }
        List<UserMealWithExcess> userMealWithExcessList = new ArrayList<>();
        for (UserMeal meal : meals) {
            if (isBetweenHalfOpen(meal.getDateTime().toLocalTime(), startTime, endTime)) {
                userMealWithExcessList.add(
                        new UserMealWithExcess(
                                meal.getDateTime(),
                                meal.getDescription(),
                                meal.getCalories(),
                                caloriesByDateMap.get(meal.getDateTime().toLocalDate()) > caloriesPerDay));
            }
        }
        return userMealWithExcessList;
    }

    /**
     * Альтернативная реализация метода {@link #filteredByCycles(List, LocalTime, LocalTime, int)},
     * использующая Stream API для фильтрации и обработки данных.
     *
     * @param meals          список объектов {@code UserMeal}, которые нужно отфильтровать
     * @param startTime      начало временного диапазона (включительно)
     * @param endTime        конец временного диапазона (исключительно)
     * @param caloriesPerDay максимально допустимое количество калорий в день
     * @return список объектов {@code UserMealWithExcess}, представляющих приемы пищи в заданном
     * временном интервале с указанием, превышена ли суточная норма калорий
     */
    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> caloriesByDateMap = meals.stream().collect(Collectors.toMap(
                userMeal -> userMeal.getDateTime().toLocalDate(),
                UserMeal::getCalories,
                Integer::sum
        ));
        List<UserMealWithExcess> userMealWithExcessList;
        userMealWithExcessList = meals.stream()
                .filter(userMeal -> isBetweenHalfOpen(userMeal.getDateTime().toLocalTime(), startTime, endTime))
                .map(userMeal -> new UserMealWithExcess(userMeal.getDateTime(),
                        userMeal.getDescription(),
                        userMeal.getCalories(),
                        caloriesByDateMap.get(userMeal.getDateTime().toLocalDate()) > caloriesPerDay))
                .toList();

        return userMealWithExcessList;
    }
}
