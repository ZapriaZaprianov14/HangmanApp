package com.proxiad.trainee;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class InitializingContextListener implements ServletContextListener {
  private static final int MAX_LIVES = 9;

  @Override
  public void contextInitialized(ServletContextEvent sce) {
    ServletContext context = sce.getServletContext();

    char[][] qwertyKeyboard = {
      {'Q', 'W', 'E', 'R', 'T', 'Y', 'U', 'I', 'O', 'P'},
      {'A', 'S', 'D', 'F', 'G', 'H', 'J', 'K', 'L'},
      {'Z', 'X', 'C', 'V', 'B', 'N', 'M'}
    };

    context.setAttribute("qwertyKeyboard", qwertyKeyboard);
    context.setAttribute("maxLives", MAX_LIVES);
  }
}
