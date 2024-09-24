package com.proxiad.trainee;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/ongoing")
public class OngoingGamesServlet extends HttpServlet {
  @Autowired private GameService gameService;

  @Override
  public void init() {
    ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
    gameService = context.getBean(GameService.class);
  }

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {
    HttpSession session = request.getSession();
    List<GameData> previousGames = gameService.getAllGames(session);
    // the list has to contain ongoing games
    if (containsOngoingGames(previousGames)) {
      request.setAttribute("gamesReversed", reverseListOfGames(previousGames));
      request.getRequestDispatcher("/ongoing.jsp").forward(request, response);
    } else {
      request.setAttribute("message", "No onging games.");
      request.getRequestDispatcher("/empty.jsp").forward(request, response);
    }
  }

  private boolean containsOngoingGames(List<GameData> games) {
    return games.stream().anyMatch(game -> !game.isFinished());
  }

  private List<GameData> reverseListOfGames(List<GameData> games) {
    List<GameData> result = new ArrayList<>();
    for (int i = games.size() - 1; i >= 0; i--) {
      result.add(games.get(i));
    }
    return result;
  }
}
