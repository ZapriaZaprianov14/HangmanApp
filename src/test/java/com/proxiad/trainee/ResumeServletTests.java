package com.proxiad.trainee;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.io.IOException;
import java.util.UUID;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import static com.proxiad.trainee.CategoryEnum.FRUITS;
import com.proxiad.trainee.Constants;
import com.proxiad.trainee.GameData;
import com.proxiad.trainee.GameService;
import static com.proxiad.trainee.GamemodeEnum.SINGLEPLAYER;
import com.proxiad.trainee.InvalidWordException;
import com.proxiad.trainee.NewGameServlet;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class ResumeServletTests {

  @Mock private GameService gameService;

  @Mock private HttpServletRequest request;

  @Mock private HttpServletResponse response;

  @Mock private HttpSession session;

  @Mock private RequestDispatcher requestDispatcher;

  @InjectMocks private ResumeGameServlet servlet;

  @Rule public MockitoRule rule = MockitoJUnit.rule();

  @Before
  public void setUp() {
    when(request.getSession()).thenReturn(session);
  }

  @Test
  public void testDoPost() throws ServletException, IOException {
    CategoryEnum category = FRUITS;
    GameData game = new GameData("Cherry", category, SINGLEPLAYER, 9);
    when(request.getParameter("gameId")).thenReturn(game.getId().toString());
    when(gameService.resumeGame(game.getId(), session)).thenReturn(game);
    when(request.getParameter("category")).thenReturn(category.toString());
    when(request.getRequestDispatcher("/game.jsp")).thenReturn(requestDispatcher);

    servlet.doPost(request, response);

    verify(request).getRequestDispatcher("/game.jsp");
    verify(requestDispatcher).forward(request, response);
  }
}
