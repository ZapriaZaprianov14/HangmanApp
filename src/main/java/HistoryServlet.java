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

@WebServlet("/history")
public class HistoryServlet extends HttpServlet {
  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {
    HttpSession session = request.getSession();
    List<GameData> previousGames = (List<GameData>) session.getAttribute("previousGames");

    if (previousGames == null || previousGames.isEmpty() || !containsFinishedGames(previousGames)) {
      request.setAttribute("message", "No games have been finished yet.");
      RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/empty.jsp");
      dispatcher.forward(request, response);
    } else {
      // reversing to display the last games first inside the page
      request.setAttribute("gamesReversed", reverseData(previousGames));
      RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/history.jsp");
      dispatcher.forward(request, response);
    }
  }

  private boolean containsFinishedGames(List<GameData> games) {
    return games.stream().anyMatch(game -> game.isFinished());
  }

  private List<GameData> reverseData(List<GameData> games) {
    List<GameData> result = new ArrayList<>();
    for (int i = games.size() - 1; i >= 0; i--) {
      result.add(games.get(i));
    }
    return result;
  }
}
