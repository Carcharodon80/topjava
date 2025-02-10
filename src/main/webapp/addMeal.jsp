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

<h1>Add meal</h1>
<p><a href="meals">Add Meal</a></p>
<form method="POST" action='meals' name="frmAddUser">
    Date Time : <label>
    <input type="datetime-local" name="dateTime"/>
</label> <br/>
    Description : <label>
    <input type="text" name="description"/>
</label> <br/>
    Calories : <label>
    <input type="text" name="calories"/>
</label> <br/>
    <input type="submit" value="Сохранить"/>
</form>
</body>
</html>
