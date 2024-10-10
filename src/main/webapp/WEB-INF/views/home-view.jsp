<%@ page import="static com.proxiad.trainee.Constants.HISTORY_URL" %>
<%@ page import="static com.proxiad.trainee.Constants.ONGOING_URL" %>
<%@ page import="static com.proxiad.trainee.Constants.GAMES_CONTROLLER_URL" %>
<html>
<head>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/site.css">
<title>Hangman game</title>
<script>
    function submitForm(category) {
        var form = document.getElementById('hangman-form');
        form.action = '/HangmanApp/games/game/' + category;
        form.submit();
    }
</script>
</head>
<body>
	<div class="container">
		<div class="content">
			<p>Welcome to the Hangman game</p>
			<p>Choose your category:</p>
			
			<form id="hangman-form" method="post">
			    <p>
			        <input id="cities-btn" class="generic-btn" type="submit" onclick="submitForm('Cities')" value="Cities">
			        <input id="countries-btn" class="generic-btn" type="submit" onclick="submitForm('Countries')" value="Countries">
			        <input id="places-btn" class="generic-btn" type="submit" onclick="submitForm('Places')" value="Places">
			    </p>
			    <p>
			        <input id="cars-btn" class="generic-btn" type="submit" onclick="submitForm('Cars')" value="Cars">
			        <input id="fruits-btn" class="generic-btn" type="submit" onclick="submitForm('Fruits')" value="Fruits">
			        <input id="movies-btn" class="generic-btn" type="submit" onclick="submitForm('Movies')" value="Movies">
			    </p>
			</form>
			<br>
			<form action="${GAMES_CONTROLLER_URL}/multiplayerInput">
				<input id="multiplayer-btn" class="generic-btn" type="submit" value="Start Multiplayer Game">
			</form>
			<br>
			<form action="${HISTORY_URL}">
				<input class="generic-btn" type="submit" value="View Game History">
				<input class="generic-btn" formaction="${ONGOING_URL}" type="submit"
					value="View Ongoing Games">
			</form>
		</div>
	</div>
</body>
</html>
