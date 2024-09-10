import java.io.IOException;
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

@WebServlet("/leave-game")
public class LeaveGameServlet extends HttpServlet{
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		GameData currentGame = (GameData) session.getAttribute("currentGameData");
		
		//if refresh is clicked
		if(currentGame == null) {
			RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/index.jsp");
			dispatcher.forward(request, response);
		}
		
		currentGame.setFinished(false);
		HashMap<UUID,GameData> previousGames = (HashMap<UUID, GameData>) session.getAttribute("previousGames");
		previousGames.put(currentGame.getId(),currentGame);
		
		
		session.removeAttribute("currentGameData");
		session.removeAttribute("category");
		
		RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/index.jsp");
		dispatcher.forward(request, response);
	}
}
