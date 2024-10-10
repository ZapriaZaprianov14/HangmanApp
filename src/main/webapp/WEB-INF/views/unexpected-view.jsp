<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="static com.proxiad.trainee.Constants.HOME_URL" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Unexpected error</title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/site.css">
</head>
<body>
	<div class="container">
		<div class="content">
			<h2>${message}</h2>
			<form action="${HOME_URL}" method="get">
				<input class="generic-btn" type="submit" value="Enter new Word">
			</form>
		</div>
	</div>
</body>
</html>