package com.proxiad.trainee;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
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
  public void testDoPostWithValidWord()
      throws InvalidWordException, ServletException, IOException, InvalidCategoryException {
    String category = "FRUITS";
    String word = "Peach";
    when(request.getRequestDispatcher("/game.jsp")).thenReturn(requestDispatcher);
    when(request.getParameter("word")).thenReturn(word);
    when(request.getParameter("category")).thenReturn(category);
    when(gameService.startNewGame(category, word, session)).thenReturn(new GameData());

    servlet.doPost(request, response);

    verify(request).getRequestDispatcher("/game.jsp");
    verify(requestDispatcher).forward(request, response);
  }

  @Test
  public void testDoPostWithInvalidWord()
      throws InvalidWordException, ServletException, IOException, InvalidCategoryException {
    String category = "FRUITS";
    String word = "Aa";
    when(request.getRequestDispatcher("/bad-request.jsp")).thenReturn(requestDispatcher);
    when(request.getParameter("word")).thenReturn(word);
    when(request.getParameter("category")).thenReturn(category);
    when(gameService.startNewGame(category, word, session)).thenThrow(InvalidWordException.class);

    servlet.doPost(request, response);

    verify(request).getRequestDispatcher("/bad-request.jsp");
    verify(requestDispatcher).forward(request, response);
  }

  @Test
  public void testDoPostWithInvalidCategory()
      throws InvalidWordException, ServletException, IOException, InvalidCategoryException {
    String category = "FRUI";
    String word = "Peach";
    when(request.getRequestDispatcher("/bad-request.jsp")).thenReturn(requestDispatcher);
    when(request.getParameter("word")).thenReturn(word);
    when(request.getParameter("category")).thenReturn(category);
    when(gameService.startNewGame(category, word, session))
        .thenThrow(InvalidCategoryException.class);

    servlet.doPost(request, response);

    verify(request).getRequestDispatcher("/bad-request.jsp");
    verify(requestDispatcher).forward(request, response);
  }
}
