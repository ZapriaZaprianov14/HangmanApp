<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
<link rel="stylesheet" type="text/css" href="css/site.css" />
<title>Playing</title>
</head>
<body>
	<div class="container">
		<div class="content">
			<p>Your chosen category: ${currentGameData.category}</p>
			<p class="preserve-space">Word: ${currentGameData.wordProgress}</p>
			<br>

			<c:forEach items="${qwertyKeyboard}" var="row">
				<div class="keyboard-row">
					<c:forEach items="${row}" var="letter">
						<c:choose>
							<c:when
								test="${currentGameData.unguessedLetters.contains(letter)}">
								<form class="key-form" action="game" method="post">
									<input class="key" type="submit" name="guess" value="${letter}">
								</form>
							</c:when>
							<c:otherwise>
								<form class="key-form" action="game" method="post">
									<input disabled class="key-disabled" type="submit" name="guess" value="${letter}">
								</form>
							</c:otherwise>
						</c:choose>
					</c:forEach>
				</div>
			</c:forEach>

			<br> <img class="stickman"
				src="images/hangman-${currentGameData.lives}.png">
			<form action="leave-game" method="post">
				<input class="generic-btn" type="submit" value="Leave game">
			</form>
		</div>
	</div>
</body>
</html>
