import java.io.IOException;
import java.util.List;
import java.util.UUID;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/resume-game")
public class ResumeGameServlet extends HttpServlet {
  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    HttpSession session = request.getSession();
    String stringID = request.getParameter("gameId");

    if (stringID == null) {
      response.sendRedirect("index-redirect");
      return;
    }

    UUID gameID = UUID.fromString(stringID);
    List<GameData> previousGames = (List<GameData>) session.getAttribute("previousGames");
    //    GameData gameToResume = previousGames.get(gameID);
    GameData gameToResume = getGame(previousGames, gameID);
    session.setAttribute("currentGameData", gameToResume);
    session.setAttribute("category", gameToResume);

    RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/game.jsp");
    dispatcher.forward(request, response);
  }

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    response.sendRedirect("index-redirect");
    return;
  }

  private GameData getGame(List<GameData> games, UUID id) {
    for (GameData game : games) {
      if (game.getId().equals(id)) {
        return game;
      }
    }
    return null;
  }
}
