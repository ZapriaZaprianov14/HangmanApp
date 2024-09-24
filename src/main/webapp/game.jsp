<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="com.proxiad.trainee.GamemodeEnum" %>
<html>
<head>
<link rel="stylesheet" type="text/css" href="css/site.css" />
<title>Playing</title>
</head>
<body>
	<div class="container">
		<div class="content">
			<c:if test="${currentGame.gamemode == GamemodeEnum.MULTIPLAYER}">
    			<p style="margin-top:1px">Player 2 guess the word</p>
			</c:if>
			<p>Category: ${currentGame.category}</p>
			<p class="preserve-space">${currentGame.wordProgress}</p>
			<c:forEach items="${qwertyKeyboard}" var="row">
				<div class="keyboard-row">
					<c:forEach items="${row}" var="letter">
						<form class="key-form" action="game" method="post">
							<input type="submit" name="guess" value="${letter}"
								${ !currentGame.unguessedLetters.contains(letter) ? 'disabled="disabled" class="key-disabled"' : 'class="key"'} />
						</form>
					</c:forEach>
				</div>
			</c:forEach>

			<img class="stickman"
				src="images/hangman-${currentGame.lives}.png">
			<form action="leave-game" method="post">
				<input class="generic-btn" type="submit" value="Leave game">
			</form>
		</div>
	</div>
</body>
</html>
