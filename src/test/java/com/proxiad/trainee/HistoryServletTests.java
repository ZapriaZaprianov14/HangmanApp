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
import com.proxiad.trainee.HistoryServlet;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class HistoryServletTests {

  @Mock private HttpSession session;

  @Mock private HttpServletRequest request;

  @Mock private HttpServletResponse response;

  @Mock private RequestDispatcher requestDispatcher;

  @Mock private GameService gameService;

  @InjectMocks private HistoryServlet servlet;

  @Rule public MockitoRule rule = MockitoJUnit.rule();

  @Before
  public void setup() {
    when(request.getSession()).thenReturn(session);
  }

  @Test
  public void whenFinishedGamesPresent() throws IOException, ServletException {
    List<GameData> games = Arrays.asList(new GameData(), new GameData());
    when(gameService.reverseListOfGames(games)).thenReturn(games);
    when(gameService.getAllGames(session)).thenReturn(games);
    when(gameService.containsFinishedGames(games)).thenReturn(true);
    when(request.getRequestDispatcher("/history.jsp")).thenReturn(requestDispatcher);

    servlet.doGet(request, response);

    verify(request).setAttribute(eq("gamesReversed"), eq(games));
    verify(request).getRequestDispatcher("/history.jsp");
    verify(requestDispatcher).forward(request, response);
  }

  @Test
  public void whenNoFinishedGamesPresent() throws IOException, ServletException {
    List<GameData> games = new ArrayList<GameData>();
    when(gameService.reverseListOfGames(games)).thenReturn(games);
    when(gameService.getAllGames(session)).thenReturn(null);
    when(gameService.containsFinishedGames(games)).thenReturn(false);
    when(request.getRequestDispatcher("/empty.jsp")).thenReturn(requestDispatcher);

    servlet.doGet(request, response);

    verify(request).setAttribute(eq("message"), eq("No games have been finished yet."));
    verify(request).getRequestDispatcher("/empty.jsp");
    verify(requestDispatcher).forward(request, response);
  }
}
