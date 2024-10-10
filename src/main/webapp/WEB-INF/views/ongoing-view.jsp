<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="static com.proxiad.trainee.Constants.HOME_URL" %>
<%@ page import="static com.proxiad.trainee.Constants.GAMES_CONTROLLER_URL" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Ongoing Games</title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/site.css">
<script>
/*
function submitForm(gameId) {
    var form = document.getElementById('resume-form');
    form.action = '/HangmanApp/games/' + gameId + '/resume';
    form.submit();
}
*/
function submitFormEvent(gameId) {
	var form = document.getElementById('resume-form-' + gameId);
    form.action = '/HangmanApp/games/' + gameId + '/resume';
    form.submit();
    return false;
}
</script>
</head>
<body>
	<div class="container">
		<div class="content">
			<table>
				<thead>
					<tr>
						<th>Progress</th>
						<th>Category</th>
						<th>Guesses Made</th>
						<th>Lives Left</th>
						<th>Gamemode</th>
						<th></th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${gamesReversed}" var="game">
						<c:if test="${!game.finished}">
							<tr name="game-row">
								<td class="preserve-space">${game.wordProgress}</td>
								<td>${game.category}</td>
								<td>${game.guessesMade}</td>
								<td>${game.lives}</td>
								<td>${game.gamemode}</td>
								<td>
									<form onsubmit="submitFormEvent('${game.id}')" id="resume-form-${game.id}" method="post">
									    <input name="resume-btn" class="small-btn generic-btn" type="submit" value="Resume">
									</form>
								</td>
							</tr>
						</c:if>
					</c:forEach>
				</tbody>
			</table>
			
			<form style="margin:20px" action="${HOME_URL}" method="get">
				<input id="home-btn" class="generic-btn" type="submit" value="Home">
			</form>
		</div>
	</div>
</body>
</html>