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
import java.util.Arrays;
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
import org.springframework.boot.test.context.SpringBootTest;
import com.proxiad.trainee.GameData;
import com.proxiad.trainee.exceptions.GameNotFoundException;
import com.proxiad.trainee.interfaces.GameRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class GameRepositoryTests {

  @Mock HttpSession session;

  @Rule public MockitoRule rule = MockitoJUnit.rule();

  @Autowired private GameRepository repository;

  public GameRepository getRepository() {
    return repository;
  }

  public void setRepository(GameRepository repository) {
    this.repository = repository;
  }

  @Test
  public void testReturnsAllFinishedGames() {
    List<GameData> games = new ArrayList<GameData>();
    GameData game1 = new GameData();
    GameData game2 = new GameData();
    game2.setFinished(true);
    games.add(game1);
    games.add(game2);
    when(session.getAttribute(PREVIOUS_GAMES)).thenReturn(games);

    List<GameData> returnedGames = repository.getAllFinishedGames(session);

    assertThat(returnedGames).isEqualTo(Arrays.asList(game2));
  }

  @Test
  public void testReturnsAllOngoingGames() {
    List<GameData> games = new ArrayList<GameData>();
    GameData game1 = new GameData();
    GameData game2 = new GameData();
    game2.setFinished(true);
    games.add(game1);
    games.add(game2);
    when(session.getAttribute(PREVIOUS_GAMES)).thenReturn(games);

    List<GameData> returnedGames = repository.getAllOngoingGames(session);

    assertThat(returnedGames).isEqualTo(Arrays.asList(game1));
  }

  @Test
  public void testThrowsExceptionWhenGameNotFound() throws GameNotFoundException {
    List<GameData> games = new ArrayList<GameData>();
    when(session.getAttribute(PREVIOUS_GAMES)).thenReturn(games);

    assertThatThrownBy(
            () -> {
              repository.getGame(1000L, session);
            })
        .isInstanceOf(GameNotFoundException.class)
        .hasMessageContaining("This game does not exist.");
  }

  @Test
  public void testReturnsCorrectGame() throws GameNotFoundException {
    GameData game = new GameData();
    List<GameData> games = Collections.singletonList(game);
    when(session.getAttribute(PREVIOUS_GAMES)).thenReturn(games);

    GameData returnedData = repository.getGame(game.getId(), session);

    assertThat(returnedData).isEqualTo(game);
  }

  @Test
  public void testSavesGameCorrectly() {
    GameData game = new GameData();
    List<GameData> games = new ArrayList<>();
    when(session.getAttribute(PREVIOUS_GAMES)).thenReturn(games);

    repository.postGame(game, session);

    verify(session).setAttribute(eq(PREVIOUS_GAMES), anyList());
    assertFalse(games.isEmpty());
    assertTrue(games.size() == 1);
  }

  @Test
  public void testUpdatesCorrectGame() throws GameNotFoundException {
    GameData game = new GameData();
    game.setId(0L);
    game.setWord("HONDA");
    GameData updateData = new GameData();
    updateData.setId(0L);
    updateData.setWord("TOYOTA");
    List<GameData> games = new ArrayList<GameData>();
    games.add(game);
    when(session.getAttribute(PREVIOUS_GAMES)).thenReturn(games);

    repository.updateGame(updateData, session);

    assertTrue(games.get(0).getWord().equals("TOYOTA"));
  }
}
