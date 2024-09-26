package com.proxiad.trainee;

import static org.mockito.ArgumentMatchers.eq;
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
import com.proxiad.trainee.CategoryEnum;
import com.proxiad.trainee.Constants;
import com.proxiad.trainee.GameData;
import com.proxiad.trainee.GameService;
import com.proxiad.trainee.GamemodeEnum;
import com.proxiad.trainee.InvalidWordException;
import com.proxiad.trainee.NewGameServlet;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class NewGameServletTests {

  @Mock private GameService gameService;

  @Mock private HttpServletRequest request;

  @Mock private HttpServletResponse response;

  @Mock private HttpSession session;

  @Mock private RequestDispatcher requestDispatcher;

  @InjectMocks private NewGameServlet servlet;

  @Rule public MockitoRule rule = MockitoJUnit.rule();

  @Before
  public void setUp() {
    when(request.getSession()).thenReturn(session);
  }

  @Test
  public void testDoPost() throws InvalidWordException, ServletException, IOException {
    String category = "FRUITS";
    String word = "Peach";
    GameData game =
        new GameData(word, CategoryEnum.FRUITS, GamemodeEnum.SINGLEPLAYER, Constants.MAX_LIVES);
    when(gameService.startNewGame(category, word, session)).thenReturn(game);
    when(request.getRequestDispatcher("/game.jsp")).thenReturn(requestDispatcher);

    servlet.doPost(request, response);

    verify(request).setAttribute(eq("currentGame"), eq(game));
    verify(request).getRequestDispatcher("/game.jsp");
    verify(requestDispatcher).forward(request, response);
  }
}
