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
			<p>Word: ${currentGameData.wordProgress}</p>
			<br>
			<div class="keyboard">
				<c:forEach items="${alphabet}" var="letter">
					<c:choose>
						<c:when
							test="${currentGameData.unguessedLetters.contains(letter)}">
							<form action="game" method="post">
								<input class="key" type="submit" name="guess" value="${letter}">
							</form>
						</c:when>
						<c:otherwise>
							<button disabled>${letter}</button>
						</c:otherwise>
					</c:choose>
				</c:forEach>
			</div>
			<br> <img class="stickman"
				src="images/hangman-${currentGameData.lives}.png">
			<form action="leave-game" method="post">
				<input class="generic-btn" type="submit" value="Leave game">
			</form>
		</div>
	</div>
</body>
</html>
