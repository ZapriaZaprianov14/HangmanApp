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

@WebServlet("/leave-game")
public class LeaveGameServlet extends HttpServlet {

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
    gameService.leaveGame(session);
    request.getRequestDispatcher("/home.jsp").forward(request, response);
  }
}
