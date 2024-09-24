<html>
<head>
<link rel="stylesheet" type="text/css" href="css/site.css" />
<title>Hangman game</title>
</head>
<body>
	<div class="container">
		<div class="content">
			<p>Welcome to the Hangman game</p>
			<p>Choose your category:</p>


			<form action="new-game" method="post">
				<p>
					 <input class="generic-btn" type="submit"
						name="category" value="Cities">
					 <input class="generic-btn" type="submit"
						name="category" value="Countries">
					 <input class="generic-btn" type="submit" 
						name="category" value="Places">
				</p>
				<p>
					<input class="generic-btn" type="submit" 
						name="category" value="Cars">
					<input class="generic-btn" type="submit"
						name="category" value="Fruits">
					<input class="generic-btn" type="submit"
						name="category" value="Movies">
				</p>
			</form>


			<br>
			<p>
				<form action="multiplayer.jsp">
					<input class="generic-btn" type="submit" value="Start Multiplayer Game">
				</form>
			</p>
			<br>
			<form action="history">
				<input class="generic-btn" type="submit" value="View Game History">
				<input class="generic-btn" formaction="ongoing" type="submit"
					value="View Ongoing Games">
			</form>

		</div>
	</div>
</body>
</html>
