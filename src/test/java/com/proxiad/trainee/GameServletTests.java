package com.proxiad.trainee;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.io.IOException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class GameServletTests {

  @Mock private GameService gameService;

  @Mock private HttpServletRequest request;

  @Mock private HttpServletResponse response;

  @Mock private HttpSession session;

  @Mock private RequestDispatcher requestDispatcher;

  @InjectMocks private GameServlet servlet;

  @Rule public MockitoRule rule = MockitoJUnit.rule();

  @Before
  public void setUp() {
    when(request.getSession()).thenReturn(session);
  }

  @Test
  public void callsGameJSPIfGameNotFinished()
      throws InvalidWordException, ServletException, IOException, InvalidGuessException {
    GameData game = new GameData();
    game.setFinished(false);
    String guess = "A";
    doTest(game, guess, "/game.jsp");
  }

  @Test
  public void callsWinJSPIfGameWon()
      throws InvalidWordException, ServletException, IOException, InvalidGuessException {
    GameData game = new GameData();
    game.setFinished(true);
    game.setGameWon(true);
    String guess = "A";
    doTest(game, guess, "/win.jsp");
  }

  @Test
  public void callsLossJSPIfGameLost()
      throws InvalidWordException, ServletException, IOException, InvalidGuessException {
    GameData game = new GameData();
    game.setFinished(true);
    game.setGameWon(false);
    String guess = "A";
    doTest(game, guess, "/loss.jsp");
  }

  @Test
  public void callsGameJSPIfGuessIsNull()
      throws InvalidWordException, ServletException, IOException, InvalidGuessException {
    GameData game = new GameData();
    String guess = null;
    doTest(game, guess, "/game.jsp");
  }

  @Test
  public void callsBadRequestWhenGuessIsInvalid()
      throws InvalidGuessException, IOException, ServletException {
    GameData game = new GameData();
    String guess = "!";
    when(gameService.getCurrentGame(session)).thenReturn(game);
    when(gameService.makeTry(game, guess, session)).thenThrow(InvalidGuessException.class);
    when(request.getParameter("guess")).thenReturn(guess);
    when(request.getRequestDispatcher("/bad-request.jsp")).thenReturn(requestDispatcher);

    servlet.doPost(request, response);

    verify(request).getRequestDispatcher("/bad-request.jsp");
    verify(requestDispatcher).forward(request, response);
  }

  private void doTest(GameData game, String guess, String page)
      throws InvalidGuessException, IOException, ServletException {
    when(gameService.getCurrentGame(session)).thenReturn(game);
    when(gameService.makeTry(game, guess, session)).thenReturn(game);
    when(request.getParameter("guess")).thenReturn(guess);
    when(request.getRequestDispatcher(page)).thenReturn(requestDispatcher);

    servlet.doPost(request, response);

    verify(request).getRequestDispatcher(page);
    verify(requestDispatcher).forward(request, response);
  }
}
