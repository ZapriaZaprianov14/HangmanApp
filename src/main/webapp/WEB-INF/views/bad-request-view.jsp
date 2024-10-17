<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="static com.proxiad.trainee.Constants.HOME_URL" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Bad Request</title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/site.css">
</head>
<body>
	<div class="container">
		<div class="content">
			<h2>${message}</h2>
			<a class="generic-btn" href="${HOME_URL}">Home</a>
		</div>
	</div>
</body>
</html>