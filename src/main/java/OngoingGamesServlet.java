import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/ongoing")
public class OngoingGamesServlet extends HttpServlet {
  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {
    HttpSession session = request.getSession();
    List<GameData> previousGames = (List<GameData>) session.getAttribute("previousGames");
    if (previousGames == null
        || previousGames.isEmpty()
        || !containsUnfinishedGames(previousGames)) {
      request.setAttribute("message", "No onging games.");
      RequestDispatcher dispatcher =
          this.getServletContext().getRequestDispatcher("/empty-history.jsp");
      dispatcher.forward(request, response);
    } else {
      request.setAttribute("gamesReversed", reverseData(previousGames));
      RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/ongoing.jsp");
      dispatcher.forward(request, response);
    }
  }

  private boolean containsUnfinishedGames(List<GameData> games) {
    return games.stream().anyMatch(game -> !game.isFinished());
  }

  private List<GameData> reverseData(List<GameData> games) {
    List<GameData> result = new ArrayList<>();
    for (int i = games.size() - 1; i >= 0; i--) {
      result.add(games.get(i));
    }
    return result;
  }
}
