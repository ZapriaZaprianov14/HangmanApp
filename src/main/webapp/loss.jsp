<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Lost game</title>
<link rel="stylesheet" type="text/css" href="css/site.css" />
</head>
<body>
	<div class="container">
		<div class="content">
			<h1>You lost :(</h1>
			<p>The word was: ${word}</p>
			<img class="stickman-big" src="images/hangman-0.png">
			<form action="home" method="get">
				<input class="generic-btn" type="submit" value="Start new game">
			</form>
		</div>
	</div>
</body>
</html>

