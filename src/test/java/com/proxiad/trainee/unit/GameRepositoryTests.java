package com.proxiad.trainee.unit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static com.proxiad.trainee.Constants.CURRENT_GAME;
import static com.proxiad.trainee.Constants.PREVIOUS_GAMES;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.beans.factory.annotation.Autowired;
import com.proxiad.trainee.GameData;
// import com.proxiad.trainee.RootConfig;
import com.proxiad.trainee.exceptions.GameNotFoundException;
import com.proxiad.trainee.interfaces.GameRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
// @ContextConfiguration(classes = RootConfig.class)
@WebAppConfiguration
public class GameRepositoryTests {

  // add test for getAllFinished/OngoingGames
  @Mock HttpSession session;

  @Rule public MockitoRule rule = MockitoJUnit.rule();

  @Autowired private GameRepository repository;

  @Test
  public void returnsNullWhenListEmpty() {
    when(session.getAttribute(PREVIOUS_GAMES)).thenReturn(null);

    List<GameData> returnedGames = repository.getAllGames(session);

    assertThat(returnedGames).isEqualTo(null);
  }

  @Test
  public void returnsAllGames() {
    when(session.getAttribute(PREVIOUS_GAMES)).thenReturn(new ArrayList<GameData>());

    List<GameData> returnedGames = repository.getAllGames(session);

    assertThat(returnedGames).isEqualTo(new ArrayList<GameData>());
  }

  @Test
  public void returnsNullWhenGameNotFound() throws GameNotFoundException {
    List<GameData> games = new ArrayList<GameData>();
    when(session.getAttribute(PREVIOUS_GAMES)).thenReturn(games);

    assertThatThrownBy(
            () -> {
              repository.getGame(1000, session);
            })
        .isInstanceOf(GameNotFoundException.class)
        .hasMessageContaining("This game does not exist.");
  }

  @Test
  public void returnsCorrectGame() throws GameNotFoundException {
    GameData game = new GameData();
    List<GameData> games = Collections.singletonList(game);
    when(session.getAttribute(PREVIOUS_GAMES)).thenReturn(games);

    GameData returnedData = repository.getGame(game.getId(), session);

    assertThat(returnedData).isEqualTo(game);
  }

  @Test
  public void returnsCurrentGame() throws GameNotFoundException {
    GameData game = new GameData();
    when(session.getAttribute(CURRENT_GAME)).thenReturn(game);

    GameData returnedGame = repository.getCurrentGame(session);

    assertThat(game).isEqualTo(returnedGame);
  }

  @Test
  public void savesGameCorrectly() {
    GameData game = new GameData();
    List<GameData> games = new ArrayList<>();
    when(session.getAttribute(PREVIOUS_GAMES)).thenReturn(games);

    repository.saveGame(game, session);

    verify(session).setAttribute(eq(PREVIOUS_GAMES), anyList());
    assertFalse(games.isEmpty());
    assertTrue(games.size() == 1);
  }

  @Test
  public void leavesGame() throws GameNotFoundException {
    GameData game = new GameData();
    List<GameData> previousGames = new ArrayList<GameData>();
    when(session.getAttribute(CURRENT_GAME)).thenReturn(game);
    when(session.getAttribute(PREVIOUS_GAMES)).thenReturn(previousGames);

    repository.leaveGame(session);

    verify(session).removeAttribute(eq(CURRENT_GAME));
    assertFalse(previousGames.isEmpty());
    assertTrue(previousGames.size() == 1);
  }

  @Test
  public void resumesGame() throws GameNotFoundException {
    List<GameData> games = new ArrayList<GameData>();
    GameData game = new GameData();
    games.add(game);
    when(session.getAttribute(PREVIOUS_GAMES)).thenReturn(games);

    GameData returnedGame = repository.resumeGame(game.getId(), session);

    verify(session).setAttribute(CURRENT_GAME, game);
    assertThat(returnedGame).isEqualTo(game);
    assertThat(games).doesNotContain(game);
  }
}
