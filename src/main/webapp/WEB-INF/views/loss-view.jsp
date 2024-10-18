<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="static com.proxiad.trainee.Constants.HOME_URL" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Lost game</title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/site.css">
</head>
<body>
	<div class="container">
		<div class="content">
			<h1>You lost :(</h1>
			<p>The word was: ${word}</p>
			<img class="stickman-big" src="${pageContext.request.contextPath}/images/hangman-0.png">
			<p>
				<a class="generic-btn" href="${HOME_URL}">Home</a>
			</p>
		</div>
	</div>
</body>
</html>

