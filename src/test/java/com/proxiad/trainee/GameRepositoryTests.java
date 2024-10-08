package com.proxiad.trainee;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.beans.factory.annotation.Autowired;
import com.proxiad.trainee.config.RootConfig;
import com.proxiad.trainee.exceptions.GameNotFoundException;
import com.proxiad.trainee.interfaces.GameRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = RootConfig.class)
@WebAppConfiguration
public class GameRepositoryTests {

  @Mock HttpSession session;

  @Rule public MockitoRule rule = MockitoJUnit.rule();

  @Autowired private GameRepository repository;

  public void setRepository(GameRepository repository) {
    this.repository = repository;
  }

  public GameRepository getRepository() {
    return this.repository;
  }

  @Test
  public void repoReturnsNullWhenListEmpty() {
    when(session.getAttribute("previousGames")).thenReturn(null);
    assertThat(repository.getAllGames(session)).isEqualTo(null);
  }

  @Test
  public void repoDoesReturnsAllGames() {
    when(session.getAttribute("previousGames")).thenReturn(new ArrayList<GameData>());

    assertThat(repository.getAllGames(session)).isEqualTo(new ArrayList<GameData>());
  }

  @Test
  public void returnsNullWhenGameNotFound() throws GameNotFoundException {
    List<GameData> games = new ArrayList<GameData>();
    when(session.getAttribute("previousGames")).thenReturn(games);

    assertThatThrownBy(
            () -> {
              repository.getGame(UUID.randomUUID(), session);
            })
        .isInstanceOf(GameNotFoundException.class)
        .hasMessageContaining("This game does not exist.");
  }

  @Test
  public void returnsCorrectGame() throws GameNotFoundException {
    List<GameData> games = new ArrayList<GameData>(); // singleton list
    GameData game = new GameData();
    games.add(game);
    when(session.getAttribute("previousGames")).thenReturn(games);

    GameData returnedData = repository.getGame(game.getId(), session);

    assertThat(returnedData).isEqualTo(game);
  }

  @Test
  public void returnsCurrentGame() throws GameNotFoundException {
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
  public void leavesGame() throws GameNotFoundException {
    GameData game = new GameData();
    List<GameData> previousGames = new ArrayList<GameData>();
    when(session.getAttribute("currentGame")).thenReturn(game);
    when(session.getAttribute("previousGames")).thenReturn(previousGames);

    repository.leaveGame(session);

    verify(session).removeAttribute(eq("currentGame"));
    System.out.println(previousGames.size());
    assertFalse(previousGames.isEmpty());
    assertTrue(previousGames.size() == 1);
  }

  @Test
  public void resumesGame() throws GameNotFoundException {
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
