<%@ page import="static com.proxiad.trainee.Constants.FINISHED_GAMES_URL" %>
<%@ page import="static com.proxiad.trainee.Constants.ONGOING_GAMES_URL" %>
<%@ page import="static com.proxiad.trainee.Constants.GAMES_CONTROLLER_URL" %>
<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/site.css">
<title>Hangman game</title>
<script>
    function submitForm(category) {
        var form = document.getElementById('hangman-form');
        form.action = '/HangmanApp/api/v1/games/singleplayer/category/' + category;
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
			<p>
				<a class="generic-btn" id="multiplayer-btn" href="${GAMES_CONTROLLER_URL}/multiplayer">Start Multiplayer Game</a>
			</p>
			<br>
			<p>
				<a id="history-btn" href="${FINISHED_GAMES_URL}" class="generic-btn">View Game History</a>
				<a id="ongoing-btn" href="${ONGOING_GAMES_URL}" class="generic-btn">View Ongoing Games</a>
			</p>
		</div>
	</div>
</body>
</html>
