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

@WebServlet("/view-history")
public class HistoryServlet extends HttpServlet {
  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {
    HttpSession session = request.getSession();
    HashMap<UUID, GameData> previousGames =
        (HashMap<UUID, GameData>) session.getAttribute("previousGames");
    
    if (previousGames == null || previousGames.isEmpty() || !containsFinishedGames(previousGames)) {
      request.setAttribute("message", "No games have been finished yet.");
      RequestDispatcher dispatcher =
          this.getServletContext().getRequestDispatcher("/empty-history.jsp");
      dispatcher.forward(request, response);
    } else {
      RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/history.jsp");
      dispatcher.forward(request, response);
    }
  }
  
  private boolean containsFinishedGames(HashMap<UUID, GameData> games){
    for(GameData game : games.values()) {
      if(game.isFinished()) {
        return true;
      }
    }
    return false;
  }
  
}
