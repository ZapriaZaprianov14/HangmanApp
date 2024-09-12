<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
<meta charset="UTF-8">
<title>Game history</title>
<link rel="stylesheet" type="text/css" href="css/site.css" />
</head>
<body>
	<div class="container">
		<div class="content">
			<h2 style="margin-top: 0;">Unfinished Games</h2>
			<table>
				<thead>
					<tr>
						<th>Progress</th>
						<th>Category</th>
						<th>Lives Left</th>
						<th></th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${previousGames}" var="game">
						<c:if test="${!game.value.finished}">
							<tr>
								<td class="preserve-space">${game.value.wordProgress}</td>
								<td>${game.value.category}</td>
								<td>${game.value.lives}</td>
								<td>
									<form action="resume-game" method="post">
										<input type="hidden" name="gameId" value="${game.value.id}" />
										<input class="small-btn generic-btn" type="submit"
											name="actionName" value="Resume">
									</form>
								</td>
							</tr>
						</c:if>
					</c:forEach>
				</tbody>
			</table>

			<h2>Finished Games</h2>
			<table>
				<thead>
					<tr>
						<th>Word</th>
						<th>Category</th>
						<th>Number of Mistakes</th>
						<th>Status</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${previousGames}" var="game">
						<c:if test="${game.value.finished}">
							<c:choose>
								<c:when test="${game.value.gameWon}">
									<tr>
										<td>${game.value.word}</td>
										<td>${game.value.category}</td>
										<td>${9 - game.value.lives}</td>
										<td>Won <b class="check-mark">✔</b></td>
									</tr>
								</c:when>
								<c:otherwise>
									<tr>
										<td>${game.value.word}</td>
										<td>${game.value.category}</td>
										<td>-</td>
										<td>Lost <b class="cross-mark">❌</b></td>
									</tr>
								</c:otherwise>
							</c:choose>
						</c:if>
					</c:forEach>
				</tbody>
			</table>
			<form action="index-redirect" method="get">
				<input class="key" type="submit" value="Start new Game">
			</form>
		</div>
	</div>
</body>
</html>