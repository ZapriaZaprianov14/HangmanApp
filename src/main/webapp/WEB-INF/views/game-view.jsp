<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ page import="static com.proxiad.trainee.enums.GamemodeEnum.MULTIPLAYER" %>
<%@ page import="static com.proxiad.trainee.Constants.GAMES_CONTROLLER_URL" %>
<%@ page import="static com.proxiad.trainee.Constants.HOME_URL" %>
<%@ page import="static com.proxiad.trainee.Constants.QWERTY_KEYBOARD" %>
<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/site.css">
<title>Playing</title>
<script>
function submitForm(letterGuessed,gameId) {
    var form = document.getElementById('keyboard-form');
    form.action = '/HangmanApp/api/v1/games/' + gameId + '/guess/' + letterGuessed;
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
			<p id="category">Category: ${currentGame.category}</p>
			<p id="word-progress" class="preserve-space">${currentGame.wordProgress}</p>
			<form id="keyboard-form" method="post">
				<c:forEach items="${QWERTY_KEYBOARD}" var="row">
					<div class="keyboard-row">
						<c:forEach items="${row}" var="letter">
								<input id="letter-${letter}" onclick="submitForm('${letter}',${currentGame.id})" type="submit" name="guess" value="${letter}"
									${ !currentGame.unguessedLetters.contains(letter) ? 
									'disabled="disabled" class="key-disabled"' : 'class="key"'} />
							
						</c:forEach>
					</div>
				</c:forEach>
			</form>
			<img class="stickman"
				src="${pageContext.request.contextPath}/images/hangman-${currentGame.lives}.png">
			<br/>
			<a id="leave-btn" class="generic-btn" href="${HOME_URL}">Leave game</a>
		</div>
	</div>
</body>
</html>
