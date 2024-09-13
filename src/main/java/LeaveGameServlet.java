import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/leave-game")
public class LeaveGameServlet extends HttpServlet {
  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    HttpSession session = request.getSession();
    GameData currentGame = (GameData) session.getAttribute("currentGameData");

    // if refresh is clicked
    if (currentGame == null) {
      response.sendRedirect("index-redirect");
      return;
    }

    currentGame.setFinished(false);
    List<GameData> previousGames = (List<GameData>) session.getAttribute("previousGames");
    previousGames.add(currentGame);

    session.removeAttribute("currentGameData");
    session.removeAttribute("category");

    response.sendRedirect("index-redirect");
    return;
  }

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    response.sendRedirect("index-redirect");
    return;
  }
}
