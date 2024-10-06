package com.proxiad.trainee;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import jakarta.servlet.http.HttpSession;

public class GameRepositoryTests {

  @Mock HttpSession session;

  @Rule public MockitoRule rule = MockitoJUnit.rule();

  private GameRepository repository;

  @Before
  public void setUp() {
    ApplicationContext context = new ClassPathXmlApplicationContext("ApplicationContext.xml");
    repository = context.getBean(GameRepository.class);
  }

  @Test
  public void repoReturnsNullWhenListEmpty() {
    when(session.getAttribute("previousGames")).thenReturn(null);
    assertThat(repository.getAllGames(session)).isEqualTo(null);
  }

  @Test
  public void repoDoesReturnsAllGames() {
    //    List<GameData> games = new ArrayList<GameData>();

    when(session.getAttribute("previousGames")).thenReturn(new ArrayList<GameData>());

    assertThat(repository.getAllGames(session)).isEqualTo(new ArrayList<GameData>());
  }

  @Test
  public void returnsNullWhenGameNotFound() {
    List<GameData> games = new ArrayList<GameData>();
    when(session.getAttribute("previousGames")).thenReturn(games);

    GameData returnedData = repository.getGame(UUID.randomUUID(), session);

    assertNull(returnedData);
  }

  @Test
  public void returnsCorrectGame() {
    List<GameData> games = new ArrayList<GameData>(); // singleton list
    GameData game = new GameData();
    games.add(game);
    when(session.getAttribute("previousGames")).thenReturn(games);

    GameData returnedData = repository.getGame(game.getId(), session);

    assertThat(returnedData).isEqualTo(game);
  }

  @Test
  public void returnsCurrentGame() {
    GameData game = new GameData();
    when(session.getAttribute("currentGame")).thenReturn(game);

    GameData returnedGame = repository.getCurrentGame(session);

    assertThat(game).isEqualTo(returnedGame);
  }

  @Test
  public void savesGameCorrectly() {
    GameData game = new GameData();
    List<GameData> games = new ArrayList<>();
    when(session.getAttribute("previousGames")).thenReturn(games);

    repository.saveGame(game, session);

    verify(session).setAttribute(eq("previousGames"), anyList());
    // fails
    assertFalse(games.isEmpty());
    assertTrue(games.size() == 1);
  }

  @Test
  public void leavesGame() {
    GameData game = new GameData();
    List<GameData> games = new ArrayList<GameData>();
    when(session.getAttribute("currentGame")).thenReturn(game);
    when(session.getAttribute("previousGames")).thenReturn(games);

    repository.leaveGame(session);

    verify(session).removeAttribute(eq("currentGame"));
    assertTrue(games.isEmpty());
    assertFalse(games.size() == 1);
  }

  @Test
  public void resumesGame() {
    List<GameData> games = new ArrayList<GameData>();
    GameData game = new GameData();
    games.add(game);
    when(session.getAttribute("previousGames")).thenReturn(games);

    GameData returnedGame = repository.resumeGame(game.getId(), session);

    verify(session).setAttribute("currentGame", game);
    assertThat(returnedGame).isEqualTo(game);
    assertThat(games).doesNotContain(game);
  }
}
