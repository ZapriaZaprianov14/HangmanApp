<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ page import="com.proxiad.trainee.enums.CategoryEnum"%>
<%@ page import="static com.proxiad.trainee.Constants.HOME_URL"%>
<%@ page
	import="static com.proxiad.trainee.Constants.GAMES_CONTROLLER_URL"%>
<html>
<head>
<meta charset="UTF-8">
<title>Multiplayer</title>
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/css/site.css">
<script>
	  document.addEventListener('DOMContentLoaded', (event) => {
	      document.body.addEventListener('copy', (e) => {
	          e.preventDefault();
	          alert('Copying is disabled on this page.');
	      });
	  });
	  
	  function submitForm() {
	        event.preventDefault();
	        var form = document.getElementById('enterWordForm');
	        form.action = '/HangmanApp/games' + '/newGameMultiplayer';
	        form.submit();
	    }
	  
       function validateInput(event) {
           const input = event.target.value;
           const regex = /^[a-zA-Z\s-]{3,}$/;
           if (!regex.test(input)) {
               event.target.setCustomValidity("Word should be at least 3 letters. Special characters are not allowed");
           } else {
           	
               event.target.setCustomValidity("");
           }
       }
	  
    </script>
</head>
<body>
	<div class="container">
		<div class="content">
			<h4>Player 1 enter the word</h4>
			<form:form  class="simple-container" action="${GAMES_CONTROLLER_URL}/newGame/multiplayer"
				modelAttribute="newGameDTO" method="post">
				<table class="content">
					<tr>
						<td>Word:</td>
						<td><form:input path="wordToGuess" required="true"/></td>
						<td><form:errors path="wordToGuess" cssClass="error" /></td>
					</tr>
					<tr>
						<td>Category:</td>
						<td><form:input path="category" required="true"/></td>
						<td><form:errors path="category" cssClass="error" /></td>
					</tr>
					<tr>
						<td><form:hidden path="gamemode" value="MULTIPLAYER" /></td>
					</tr>
					<tr>
						<td class="content"><button type="submit" class="small-btn generic-btn">Enter Word</button></td>
					</tr>
				</table>
			</form:form>
			<form action="${HOME_URL}" method="get">
				<input class="generic-btn" type="submit" value="Home">
			</form>
		</div>
	</div>
</body>
</html>