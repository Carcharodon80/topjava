package ru.javawebinar.topjava.service;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.store.MealStoreMap;

import java.util.ArrayList;
import java.util.List;

public class MealService {
private final MealStoreMap meals;

    public MealService(MealStoreMap meals) {
        this.meals = meals;
    }

    public List<Meal> findAll() {
        return new ArrayList<>(meals.findAll());
    }

    public Meal findMealById(int id) {
        return meals.findById(id);
    }

    public void addMeal(Meal meal) {
        meals.add(meal);
    }

    public void deleteMeal(int id) {
        meals.delete(id);
    }
}
