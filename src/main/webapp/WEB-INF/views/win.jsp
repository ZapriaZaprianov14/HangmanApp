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
			<form action="${HOME_URL}" method="get">
				<input class="generic-btn" type="submit" value="Home">
			</form>
		</div>
	</div>
</body>
</html>