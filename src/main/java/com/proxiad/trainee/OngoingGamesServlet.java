package com.proxiad.trainee;

import java.io.IOException;
import java.util.List;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/ongoing")
public class OngoingGamesServlet extends HttpServlet {
  private GameService gameService;

  @Override
  public void init() {
    ApplicationContext context =
        new ClassPathXmlApplicationContext(Constants.CONFIGURATION_FILE_NAME);
    gameService = context.getBean(GameService.class);
  }

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {
    HttpSession session = request.getSession();
    List<GameData> previousGames = gameService.getAllGames(session);
    if (gameService.containsOngoingGames(previousGames)) {
      request.setAttribute("gamesReversed", gameService.reverseListOfGames(previousGames));
      request.getRequestDispatcher("/ongoing.jsp").forward(request, response);
    } else {
      request.setAttribute("message", "No onging games.");
      request.getRequestDispatcher("/empty.jsp").forward(request, response);
    }
  }
}
