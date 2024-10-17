<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ page import="static com.proxiad.trainee.enums.GamemodeEnum.MULTIPLAYER" %>
<%@ page import="static com.proxiad.trainee.Constants.GAMES_CONTROLLER_URL" %>
<%@ page import="static com.proxiad.trainee.Constants.QWERTY_KEYBOARD" %>
<html>
<head>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/site.css">
<title>Playing</title>
<script>
function submitForm(letterGuessed) {
    var form = document.getElementById('keyboard-form-' + letterGuessed);
    form.action = '/HangmanApp/api/games/guess/' + letterGuessed;
    form.submit();
    return false;
}
</script>
</head>
<body>
	<div class="container">
		<div class="content">
			<c:if test="${currentGame.gamemode == MULTIPLAYER}">
    			<p id="player2-msg" style="margin-top:1px">Player 2 guess the word</p>
			</c:if>
			<p>Category: ${currentGame.category}</p>
			<p id="word-progress" class="preserve-space">${currentGame.wordProgress}</p>
			<c:forEach items="${QWERTY_KEYBOARD}" var="row">
				<div class="keyboard-row">
					<c:forEach items="${row}" var="letter">
						<form onsubmit="submitForm('${letter}')" id="keyboard-form-${letter}"
						 class="key-form" method="post">
							<input id="letter-${letter}" type="submit" name="guess" value="${letter}"
								${ !currentGame.unguessedLetters.contains(letter) ? 
								'disabled="disabled" class="key-disabled"' : 'class="key"'} />
						</form>
					</c:forEach>
				</div>
			</c:forEach>

			<img class="stickman"
				src="${pageContext.request.contextPath}/images/hangman-${currentGame.lives}.png">
			<form action="${GAMES_CONTROLLER_URL}/leave" method="post">
				<input id="leave-btn" class="generic-btn" type="submit" value="Leave game">
			</form>
		</div>
	</div>
</body>
</html>
