package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.store.MealStore;
import ru.javawebinar.topjava.store.MemMealStore;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(MealServlet.class);
    private final MealStore mealStore = new MemMealStore();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");

        if (action == null) {
            action = "default";
        }

        switch (action.toLowerCase()) {
            case "add":
                log.debug("Add new meal");
                req.getRequestDispatcher("/addMeal.jsp").forward(req, resp);
                break;

            case "edit":
                log.debug("Edit existing meal");
                int id = Integer.parseInt(req.getParameter("mealId"));
                req.setAttribute("meal", mealStore.findById(id));
                req.getRequestDispatcher("/addMeal.jsp").forward(req, resp);
                break;

            case "delete":
                log.debug("Delete meal");
                try {
                    int mealId = Integer.parseInt(req.getParameter("mealId"));
                    mealStore.delete(mealId);
                } catch (NumberFormatException e) {
                    log.error("Invalid mealId for delete operation");
                }
                resp.sendRedirect(req.getContextPath() + "/meals");
                break;

            case "default":
                log.debug("Show meals");
                req.setAttribute("meals", MealsUtil.filteredByStreams(mealStore.findAll(), MealsUtil.CALORIES_PER_DAY));
                req.getRequestDispatcher("/meals.jsp").forward(req, resp);
                break;

            default:
                log.warn("Unknown action: " + action);
                req.setAttribute("meals", MealsUtil.filteredByStreams(mealStore.findAll(), MealsUtil.CALORIES_PER_DAY));
                req.getRequestDispatcher("/meals.jsp").forward(req, resp);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("UTF-8");

        String idStr = req.getParameter("id");
        String dateTimeStr = req.getParameter("dateTime");
        String description = req.getParameter("description");
        String caloriesStr = req.getParameter("calories");

        LocalDateTime dateTime = LocalDateTime.parse(dateTimeStr);
        int calories = Integer.parseInt(caloriesStr);
        Meal meal = new Meal(dateTime, description, calories);

        if (idStr != null) {
            int id = Integer.parseInt(idStr);
            meal.setId(id);
            mealStore.update(meal);
        } else {
            mealStore.add(meal);
        }
        resp.sendRedirect(req.getContextPath() + "/meals");
    }
}
