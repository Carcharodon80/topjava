<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Add Meal</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<p><a href="meals">Meals</a></p>

<h1>${meal != null ? "Edit Meal" : "Add Meal"}</h1>

<form method="POST" action="meals">
    <c:if test="${meal != null}">
        <input type="hidden" name="id" value="${meal.id}"/>
    </c:if>

    Date Time:
    <label>
        <input type="datetime-local" name="dateTime" value="${meal != null ? meal.dateTime : ''}"/>
    </label> <br/>

    Description:
    <label>
        <input type="text" name="description" value="${meal != null ? meal.description : ''}"/>
    </label> <br/>

    Calories:
    <label>
        <input type="number" min="0" name="calories" value="${meal != null ? meal.calories : ''}"/>
    </label> <br/>

    <input type="submit" value="${meal != null ? 'Update' : 'Save'}"/>
</form>
</body>
</html>
