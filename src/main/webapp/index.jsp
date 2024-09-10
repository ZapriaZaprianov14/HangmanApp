<html>
<body>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>  
<form action="new-game" method="get" style="margin-bottom: 10px">
    <input type="submit" name="category" value="cars">
    <input type="submit" name="category" value="cities">
    <input type="submit" name="category" value="fruits">
</form>
<br>
<p>Previous games:</p>
<c:forEach items="${previousGames}" var="game">  
   <c:choose>		
   		<c:when test="${!game.value.finished}">
   			<form action="resume-game" method="get" style="margin-bottom: 10px">
   			 	Progress: ${game.value.wordProgress}
   				Remaining lives: ${game.value.lives}
			    <input type="hidden" name="gameId" value="${game.value.id}" />
			    <input type="submit" name="actionName" value="Resume this Game">
			</form>
   		</c:when>
   		<c:when test="${game.value.gameWon}">
   		 	Word: ${game.value.wordProgress}
   			Remaining lives: ${game.value.lives}
   			This game has been won!
   			<br>
   		</c:when>
   		<c:otherwise>
   		 	Word: ${game.value.wordProgress}
   			This game has been lost!
   			<br>
   		</c:otherwise>
   </c:choose>
</c:forEach>
</body>
</html>
