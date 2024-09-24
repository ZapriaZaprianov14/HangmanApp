package com.proxiad.trainee;

import java.io.IOException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/game")
public class GameServlet extends HttpServlet {
  private GameData game;
  private GameService gameService;

  @Override
  public void init() {
    ApplicationContext context = new ClassPathXmlApplicationContext("ApplicationContext.xml");
    gameService = context.getBean(GameService.class);
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {
    HttpSession session = request.getSession();
    game = gameService.getCurrentGame(session);
    String guess = request.getParameter("guess");

    if (guess == null) {
      // empty request
      request.getRequestDispatcher("/game.jsp").forward(request, response);
      return;
    }

    // saves the game
    game = gameService.makeTry(game, guess.toUpperCase(), session);

    if (game.isFinished()) {
      request.setAttribute("word", game.getWordProgress());
      if (game.isGameWon()) {
        request.getRequestDispatcher("/win.jsp").forward(request, response);
      } else {
        request.getRequestDispatcher("/loss.jsp").forward(request, response);
      }
      return;
    }

    request.getRequestDispatcher("/game.jsp").forward(request, response);
  }

  // if the user types the uri into the browser
  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    request.getRequestDispatcher("/game.jsp").forward(request, response);
  }
}
