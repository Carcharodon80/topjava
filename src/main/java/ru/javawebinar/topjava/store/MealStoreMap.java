package ru.javawebinar.topjava.store;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class MealStoreMap implements Store{
    private final Map<Integer, Meal> meals = new ConcurrentHashMap<>();
    private final AtomicInteger id = new AtomicInteger(0);

    public MealStoreMap() {
        meals.put(id.get(), new Meal(id.get(), LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500));
        meals.put(id.incrementAndGet(), new Meal(id.get(), LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000));
        meals.put(id.incrementAndGet(), new Meal(id.get(), LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500));
        meals.put(id.incrementAndGet(), new Meal(id.get(), LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100));
        meals.put(id.incrementAndGet(), new Meal(id.get(), LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000));
        meals.put(id.incrementAndGet(), new Meal(id.get(), LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500));
        meals.put(id.incrementAndGet(), new Meal(id.get(), LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410));
    }

    public List<Meal> findAll() {
        return new ArrayList<>(meals.values());
    }

    public void add(Meal meal) {
        meal.setId(id.incrementAndGet());
        meals.put(meal.getId(), meal);
    }

    public Meal findById(int id) {
        return meals.get(id);
    }

    public void update(Meal meal) {
        meals.replace(meal.getId(), meal);
    }

    public void delete(int id) {
        meals.remove(id);
    }
}
