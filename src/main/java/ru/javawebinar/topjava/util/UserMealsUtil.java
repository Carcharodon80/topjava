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

        System.out.println();
        filteredByCyclesOptional2(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000).forEach(System.out::println);
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
        return meals.stream()
                .filter(userMeal -> isBetweenHalfOpen(userMeal.getDateTime().toLocalTime(), startTime, endTime))
                .map(userMeal -> new UserMealWithExcess(userMeal.getDateTime(),
                        userMeal.getDescription(),
                        userMeal.getCalories(),
                        caloriesByDateMap.get(userMeal.getDateTime().toLocalDate()) > caloriesPerDay))
                .collect(Collectors.toList());
    }

    /**
     * Альтернативная реализация метода {@link #filteredByCycles(List, LocalTime, LocalTime, int)},
     * использующая метод рекурсии.
     *
     * @param meals          список приёмов пищи (UserMeal)
     * @param startTime      начальное время для фильтрации
     * @param endTime        конечное время для фильтрации
     * @param caloriesPerDay максимальное допустимое количество калорий в день
     * @return список объектов UserMealWithExcess, содержащий приёмы пищи, соответствующие условиям
     */
    public static List<UserMealWithExcess> filteredByCyclesOptional2(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        List<UserMealWithExcess> result = new ArrayList<>();
        Map<LocalDate, Integer> caloriesByDateMap = new HashMap<>();
        int index = 0;
        return filteredWithRecursion(meals, startTime, endTime, caloriesPerDay, result, caloriesByDateMap, index);
    }

    /**
     * Рекурсивный метод, который расчитывает превышение калорий в день, а затем добавляет приемы пищи (с учетом превышения калорий),
     * попадающие в заданный временной интервал, в список {@code UserMealWithExcess}.
     *
     * @param meals             список приёмов пищи (UserMeal)
     * @param startTime         начальное время для фильтрации
     * @param endTime           конечное время для фильтрации
     * @param caloriesPerDay    максимальное допустимое количество калорий в день
     * @param result            список для сохранения результата
     * @param caloriesByDateMap карта с общей калорийностью по датам
     * @param index             текущий индекс для обработки (рекурсивный шаг)
     * @return список объектов UserMealWithExcess, соответствующий условиям фильтрации
     */
    private static List<UserMealWithExcess> filteredWithRecursion(List<UserMeal> meals, LocalTime startTime, LocalTime endTime,
                                                                  int caloriesPerDay, List<UserMealWithExcess> result,
                                                                  Map<LocalDate, Integer> caloriesByDateMap, int index) {
        if (index >= meals.size()) {
            return result;
        }

        UserMeal currentMeal = meals.get(index);
        caloriesByDateMap.merge(currentMeal.getDateTime().toLocalDate(), currentMeal.getCalories(), Integer::sum);
        filteredWithRecursion(meals, startTime, endTime, caloriesPerDay, result, caloriesByDateMap, index + 1);

        if (isBetweenHalfOpen(currentMeal.getDateTime().toLocalTime(), startTime, endTime)) {
            result.add(
                    new UserMealWithExcess(
                            currentMeal.getDateTime(),
                            currentMeal.getDescription(),
                            currentMeal.getCalories(),
                            caloriesByDateMap.get(currentMeal.getDateTime().toLocalDate()) > caloriesPerDay));
        }
        return result;
    }
}
