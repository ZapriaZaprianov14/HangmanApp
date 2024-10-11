<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
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
	  	/*
       function validateInput(event) {
           const input = event.target.value;
           const regex = /^[a-zA-Z\s-]{3,}$/;
           if (!regex.test(input)) {
               event.target.setCustomValidity("Word should be at least 3 letters. Special characters are not allowed");
           } else {
           	
               event.target.setCustomValidity("");
           }
       }
	  	*/
	  
    </script>
</head>
<body>
	<div class="container">
		<div class="content">
			<h4>Player 1 enter the word and category</h4>
			<form:form  class="simple-container" action="${GAMES_CONTROLLER_URL}/game/multiplayer"
				modelAttribute="newGameDTO" method="post">
				<table class="content">
					<tr>
						<td>Word:</td>
						<td><form:input oninput="validateInput(event)" id="wordToGuess" path="wordToGuess" required="true"/></td>
						<td><form:errors id="word-error" path="wordToGuess" cssClass="error" /></td>
					</tr>
					<tr>
						<td>Category:</td>
						<td><form:input oninput="validateInput(event)" id="category" path="category" required="true"/></td>
						<td><form:errors id="category-error" path="category" cssClass="error" /></td>
					</tr>
					<tr>
						<td><form:hidden path="gamemode" value="MULTIPLAYER" /></td>
					</tr>
					<tr>
						<td class="content"><button id="enter-btn" type="submit" class="small-btn generic-btn">Enter Word</button></td>
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