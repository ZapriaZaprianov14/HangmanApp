package com.proxiad.trainee;

import java.io.IOException;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/resume-game")
public class ResumeGameServlet extends HttpServlet {

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
    String stringID = request.getParameter("gameId");
    gameService.resumeGame(UUID.fromString(stringID), session);
    request.getRequestDispatcher("/game.jsp").forward(request, response);
  }

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    response.sendRedirect("home");
    return;
  }
}
