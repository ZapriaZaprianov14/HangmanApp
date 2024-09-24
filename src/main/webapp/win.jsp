<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Won game</title>
<link rel="stylesheet" type="text/css" href="css/site.css" />
</head>
<body>
	<div class="container">
		<div class="content">
			<h1>You won the game!!</h1>
			<p>You have successfully guessed the word: ${word}</p>
			<form action="home" method="get">
				<input class="generic-btn" type="submit" value="Start new game">
			</form>
		</div>
	</div>
</body>
</html>