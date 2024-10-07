<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%@ page import="static com.proxiad.trainee.Constants.HOME_URL" %>
<html>
<head>
<meta charset="UTF-8">
<title>${message}</title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/site.css">
</head>
<body>
	<div class="container">
		<div class="content">
			<h2>${message}</h2>
			<form action="${HOME_URL}" method="get">
				<input class="generic-btn" type="submit" value="Start new Game">
			</form>
		</div>
	</div>
</body>
</html>