package ru.javawebinar.topjava.store;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class MemMealStore implements MealStore {
    private final Map<Integer, Meal> meals = new ConcurrentHashMap<>();
    private final AtomicInteger id = new AtomicInteger(0);

    public MemMealStore() {
        List<Meal> list = Arrays.asList(
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410));
        list.forEach(this::add);
    }

    public List<Meal> findAll() {
        return new ArrayList<>(meals.values());
    }

    public Meal add(Meal meal) {
        meal.setId(id.incrementAndGet());
        return meals.put(meal.getId(), meal);
    }

    public Meal findById(int id) {
        return meals.get(id);
    }

    public Meal update(Meal meal) {
        return meals.replace(meal.getId(), meal);
    }

    public void delete(int id) {
        meals.remove(id);
    }
}
