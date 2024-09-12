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
			
				<input class="generic-btn"
					type="submit" name="category" value="Cities">
					
				<input class="generic-btn"
					type="submit" name="category" value="Countries">
					
				<input class="generic-btn"
					type="submit" name="category" value="Places">
	
			</form>
			
			<form action="new-game" method="post">
			
				<input class="generic-btn"
					type="submit" name="category" value="Cars">
					
				<input class="generic-btn"
					type="submit" name="category" value="Fruits">
					
				<input class="generic-btn"
					type="submit" name="category" value="Movies">
					
			</form>
			<br>
			<p>
				Multiplayer:
				<button disabled>Not implemented</button>
			</p>
			<br>
			<form action="view-history" method="get">
				<input class="generic-btn" type="submit" value="View Game History">
			</form>
			<form action="view-ongoing" method="get">
				<input class="generic-btn" type="submit" value="View Ongoing Games">
			</form>
			
		</div>
	</div>
</body>
</html>
