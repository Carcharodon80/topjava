package ru.javawebinar.topjava.store;

import ru.javawebinar.topjava.model.Meal;

import java.util.List;

public interface Store {
    List<Meal> findAll();

    Meal findById(int id);

    void add(Meal meal);

    void update(Meal meal);

    void delete(int id);
}
