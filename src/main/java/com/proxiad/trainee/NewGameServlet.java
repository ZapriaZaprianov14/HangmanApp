package com.proxiad.trainee;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/new-game")
public class NewGameServlet extends HttpServlet {

  @Autowired private GameService gameService;

  @Override
  public void init() {
    ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
    gameService = context.getBean(GameService.class);
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    HttpSession session = request.getSession();
    String category = request.getParameter("category");
    String word = request.getParameter("word");
    try {
      gameService.startNewGame(category, word, session);
    } catch (InvalidWordException e) {
      request.setAttribute("message", e.getMessage());
      request.getRequestDispatcher("/invalid-word.jsp").forward(request, response);
    }

    request.getRequestDispatcher("/game.jsp").forward(request, response);
  }
}
