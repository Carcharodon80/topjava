package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.store.MealStoreMap;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(MealServlet.class);
    private final MealService mealService = new MealService(new MealStoreMap());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");

        if (action == null) {
            log.debug("showMeals");
            req.setAttribute("meals", MealsUtil.fiteredByStreams(mealService.findAll(), MealsUtil.CALORIES_PER_DAY));
            req.getRequestDispatcher("/meals.jsp").forward(req, resp);
        } else if (action.equalsIgnoreCase("addMeal")) {
            log.debug("addMeal");
            req.getRequestDispatcher("/addMeal.jsp").forward(req, resp);
        } else if (action.equalsIgnoreCase("editMeal")) {
            //todo переделать на update
            log.debug("addMeal");
            req.getRequestDispatcher("/addMeal.jsp").forward(req, resp);
        } else if (action.equalsIgnoreCase("deleteMeal")) {
            log.debug("deleteMeal");
            int mealId = Integer.parseInt(req.getParameter("mealId"));
            mealService.deleteMeal(mealId);
            log.debug("showMeals");
            req.setAttribute("meals", MealsUtil.fiteredByStreams(mealService.findAll(), MealsUtil.CALORIES_PER_DAY));
            req.getRequestDispatcher("/meals.jsp").forward(req, resp);
        } else {
            log.debug("showMeals");
            req.setAttribute("meals", MealsUtil.fiteredByStreams(mealService.findAll(), MealsUtil.CALORIES_PER_DAY));
            req.getRequestDispatcher("/meals.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        String dateTimeStr = req.getParameter("dateTime");
        String description = req.getParameter("description");
        String caloriesStr = req.getParameter("calories");

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        LocalDateTime dateTime = null;
        int calories = 0;

        try {
            if (dateTimeStr != null && !dateTimeStr.isEmpty()) {
                dateTime = LocalDateTime.parse(dateTimeStr, dateTimeFormatter);
            }
            if (caloriesStr != null && !caloriesStr.isEmpty()) {
                calories = Integer.parseInt(caloriesStr);
            }
        } catch (Exception e) {
            log.error("Invalid input data");
            log.debug("showMeals");
            req.setAttribute("meals", MealsUtil.fiteredByStreams(mealService.findAll(), MealsUtil.CALORIES_PER_DAY));
            req.getRequestDispatcher("/meals.jsp").forward(req, resp);
            return;
        }

        Meal meal = new Meal(0, dateTime, description, calories);
        mealService.addMeal(meal);
        log.debug("showMeals");
        req.setAttribute("meals", MealsUtil.fiteredByStreams(mealService.findAll(), MealsUtil.CALORIES_PER_DAY));
        req.getRequestDispatcher("/meals.jsp").forward(req, resp);
    }
}
