import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/new-game")
public class NewGameServlet extends HttpServlet{
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		
		if(session.getAttribute("previousGames") == null) {
			HashMap<UUID,GameData> previousGames = new HashMap<UUID,GameData>();
			session.setAttribute("previousGames", previousGames);
		}
		
		initializeGame(request);
		RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/gameServlet");
		dispatcher.forward(request, response);
	}
	
	private void initializeGame(HttpServletRequest request){
		HttpSession session = request.getSession();
		
		String category = (String) request.getParameter("category");
		GameData gameData = new GameData(category);
		String wordToGuess = gameData.getWord();
		Character firstChar = wordToGuess.charAt(0);
		Character lastChar = wordToGuess.charAt(wordToGuess.length()-1);
		
	    gameData.getUnguessedCharacters().remove(firstChar);
	    gameData.getUnguessedCharacters().remove(lastChar);
	    
		gameData.getRightGuesses().add(firstChar);
		gameData.getRightGuesses().add(lastChar);
		
		session.setAttribute("currentGameData", gameData);
	}
}
