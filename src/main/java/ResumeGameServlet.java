import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/resume-game")
public class ResumeGameServlet extends HttpServlet{
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		//TODO have to implement the previousGames being a HashMap<UUID,GameData>
		String stringID = request.getParameter("gameId");
		UUID gameID = UUID.fromString(stringID);
		HashMap<UUID,GameData> previousGames = (HashMap<UUID, GameData>) session.getAttribute("previousGames");
		GameData gameToResume = previousGames.get(gameID);
		session.setAttribute("currentGameData", gameToResume);
		session.setAttribute("category", gameToResume);
		
		RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/game.jsp");
		dispatcher.forward(request, response);
	}
}
