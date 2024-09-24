<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="com.proxiad.trainee.CategoryEnum"%>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<link rel="stylesheet" type="text/css" href="css/site.css" />
  <script>
	  document.addEventListener('DOMContentLoaded', (event) => {
	      document.body.addEventListener('copy', (e) => {
	          e.preventDefault();
	          alert('Copying is disabled on this page.');
	      });
	  });
	  
       function validateInput(event) {
           const input = event.target.value;
           const regex = /^[a-zA-Z\s-]{3,}$/;
           if (!regex.test(input)) {
               event.target.setCustomValidity("Please enter at least 3 letters");
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
			<form action="new-game" method="post">
				<select class="multiplayer-select" name="category" required>
				<option value="" selected disabled hidden>Select a Category</option>
					<c:forEach var="option" items="${CategoryEnum.values()}">
						<option value="${option}">${option}</option>
					</c:forEach>
				</select> 
				<input class="multiplayer-input" type="text" autocomplete="off" 
				name="word" oninput="validateInput(event)" required> 
				<input class="small-btn generic-btn" type="submit" value="Enter Word">
			</form>
		</div>
	</div>
</body>
</html>