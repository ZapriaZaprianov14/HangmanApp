<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="static com.proxiad.trainee.Constants.HOME_URL" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Won game</title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/site.css">
</head>
<body>
	<div class="container">
		<div class="content">
			<h1>You won the game!!</h1>
			<p>You have successfully guessed the word: ${word}</p>
			<a class="generic-btn" href="${HOME_URL}">Home</a>
		</div>
	</div>
</body>
</html>