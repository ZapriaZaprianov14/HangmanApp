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
function submitForm(gameId) {
	var form = document.getElementById('resume-form-' + gameId);
    form.action = '/HangmanApp/api/games/' + gameId + '/resume';
    form.submit();
    return false;
}
</script>
</head>
<body>
	<div class="container">
		<div class="content">
			<c:choose>
				<c:when test="${not empty gamesReversed}">
					<table>
						<thead>
							<tr>
								<th>Progress</th>
								<th>Category</th>
								<th>Guesses Made</th>
								<th>Lives Left</th>
								<th>Gamemode</th>
								<th>Id</th>
								<th></th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${gamesReversed}" var="game">
								<tr name="game-row">
									<td class="preserve-space">${game.wordProgress}</td>
									<td>${game.category}</td>
									<td>${game.guessesMade}</td>
									<td>${game.lives}</td>
									<td>${game.gamemode}</td>
									<td>${game.id}</td>
									<td>
										<form onsubmit="submitForm('${game.id}')" id="resume-form-${game.id}" method="post">
										    <input name="resume-btn" class="small-btn generic-btn" type="submit" value="Resume">
										</form>
									</td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</c:when>
				<c:otherwise>
					<h2 id="message">No ongoing games</h2>
				</c:otherwise>
			</c:choose>
			
			<a id="home-btn" style="margin:20px" class="generic-btn" href="${HOME_URL}">Home</a>
		</div>
	</div>
</body>
</html>