package ru.javawebinar.topjava.store;

import ru.javawebinar.topjava.model.Meal;

import java.util.List;

public interface MealStore {
    List<Meal> findAll();

    Meal findById(int id);

    Meal add(Meal meal);

    Meal update(Meal meal);

    void delete(int id);
}
