<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Ongoing Games</title>
<link rel="stylesheet" type="text/css" href="css/site.css" />
</head>
<body>
	<div class="container">
		<div class="content">
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
			
			<form style="margin:20px" action="index-redirect" method="get">
				<input class="generic-btn" type="submit" value="Start new Game">
			</form>
		</div>
	</div>
</body>
</html>