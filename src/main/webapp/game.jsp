<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<%@ page isELIgnored="false" %>
<html>
<body>
	<p>Your chosen category: ${currentGameData.category}</p>
	<p>Word: ${currentGameData.wordProgress}</p>
	<p>Lives left: ${currentGameData.lives}</p>

	<form action="gameServlet" method="get">
		<input type="text" id="guess" name="guess" minlength="1" maxlength="1" size="1" pattern="[A-Za-z]" required/>
		<input type="submit" value="Submit">
	</form>
	<br>
	<c:if test="${trivialGuess != null}">
		Letter <b>${trivialGuess}</b> is already guessed.
	</c:if>
	<br>
	<form action="leave-game" method="get">
		<input type="submit" value="Leave current game">
	</form>
</body>
</html>
