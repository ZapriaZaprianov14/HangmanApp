package com.proxiad.trainee;

import java.io.IOException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/new-game")
public class NewGameServlet extends HttpServlet {

  private GameService gameService;

  @Override
  public void init() {
    ApplicationContext context =
        new ClassPathXmlApplicationContext(Constants.CONFIGURATION_FILE_NAME);
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
    } catch (InvalidWordException | InvalidCategoryException e) {
      request.setAttribute("message", e.getMessage());
      request.getRequestDispatcher("/bad-request.jsp").forward(request, response);
      return;
    }

    request.getRequestDispatcher("/game.jsp").forward(request, response);
  }

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    response.sendRedirect("home");
  }
}
