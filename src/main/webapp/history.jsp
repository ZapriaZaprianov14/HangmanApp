<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
<meta charset="UTF-8">
<title>Game History</title>
<link rel="stylesheet" type="text/css" href="css/site.css" />
</head>
<body>
	<div class="container">
		<div class="content">
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
			<form style="margin:20px" action="index-redirect" method="get">
				<input class="generic-btn" type="submit" value="Start new Game">
			</form>
		</div>
	</div>
</body>
</html>