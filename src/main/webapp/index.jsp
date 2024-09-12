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


			<form action="new-game" method="post" style="margin-bottom: 10px">
				Single-Player: <input class="small-btn generic-btn" type="submit"
					name="category" value="Cars"> <input
					class="small-btn generic-btn" type="submit" name="category"
					value="Cities"> <input class="small-btn generic-btn"
					type="submit" name="category" value="Fruits">
			</form>
			<p>
				Multiplayer:
				<button disabled>Not implemented</button>
			</p>
			<form action="view-history" method="get">
				<input class="generic-btn" type="submit" value="View Game History">
			</form>

		</div>
	</div>
</body>
</html>
