<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Share File</title>
</head>
<body>
    <h1>Share File</h1>

    <form action="FileShareAccessServlet" method="post">
        <input type="hidden" name="fileId" value="${fileId}" />
        
        <label for="sharedUsername">Share with (Username):</label>
        <input type="text" name="sharedUsername" id="sharedUsername" required />

        <button type="submit">Share File</button>
    </form>
</body>
</html>
