package com.proxiad.trainee;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import com.proxiad.trainee.GameData;
import com.proxiad.trainee.GameService;
import com.proxiad.trainee.OngoingGamesServlet;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class OngoingServletTests {

  @Mock private GameService gameService;

  @Mock private HttpServletRequest request;

  @Mock private HttpServletResponse response;

  @Mock private HttpSession session;

  @Mock private RequestDispatcher requestDispatcher;

  @InjectMocks private OngoingGamesServlet ongoingGamesServlet;

  @Rule public MockitoRule rule = MockitoJUnit.rule();

  @Before
  public void setUp() {
    when(request.getSession()).thenReturn(session);
  }

  @Test
  public void testDoGet_WithOngoingGames() throws ServletException, IOException {
    List<GameData> games = Arrays.asList(new GameData(), new GameData());
    when(gameService.getAllGames(session)).thenReturn(games);
    when(gameService.containsOngoingGames(games)).thenReturn(true);
    when(gameService.reverseListOfGames(games)).thenReturn(games);
    when(request.getRequestDispatcher("/ongoing.jsp")).thenReturn(requestDispatcher);

    ongoingGamesServlet.doGet(request, response);

    verify(request).setAttribute(eq("gamesReversed"), eq(games));
    verify(request).getRequestDispatcher("/ongoing.jsp");
    verify(requestDispatcher).forward(request, response);
  }

  @Test
  public void testDoGet_NoOngoingGames() throws ServletException, IOException {
    List<GameData> noOngoingGames = new ArrayList<GameData>();
    when(gameService.getAllGames(session)).thenReturn(noOngoingGames);
    when(gameService.containsOngoingGames(noOngoingGames)).thenReturn(false);
    when(request.getRequestDispatcher("/empty.jsp")).thenReturn(requestDispatcher);

    ongoingGamesServlet.doGet(request, response);

    verify(request).setAttribute(eq("message"), eq("No onging games."));
    verify(request).getRequestDispatcher("/empty.jsp");
    verify(requestDispatcher).forward(request, response);
  }
}
