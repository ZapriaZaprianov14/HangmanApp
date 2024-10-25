package com.proxiad.trainee.unit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import com.proxiad.trainee.GameData;
import com.proxiad.trainee.NewGameDTO;
import com.proxiad.trainee.enums.GamemodeEnum;
import com.proxiad.trainee.exceptions.GameNotFoundException;
import com.proxiad.trainee.exceptions.InvalidCategoryException;
import com.proxiad.trainee.exceptions.InvalidGuessException;
import com.proxiad.trainee.exceptions.InvalidWordException;
import com.proxiad.trainee.interfaces.GameService;
import static com.proxiad.trainee.enums.GamemodeEnum.*;
import static com.proxiad.trainee.Constants.MAX_LIVES;
import static com.proxiad.trainee.Constants.PREVIOUS_GAMES;
import jakarta.servlet.http.HttpSession;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class GameServiceTests {

  @Mock HttpSession session;

  @Rule public MockitoRule rule = MockitoJUnit.rule();

  @Autowired private GameService gameService;

  @Test
  public void startsSingleplayerGameCorrectly()
      throws InvalidWordException, InvalidCategoryException {
    NewGameDTO newGameDTO = new NewGameDTO(null, "SINGLEPLAYER", "CARS");

    GameData returnedData = gameService.startNewGame(newGameDTO, session);

    String wordProgress = returnedData.getWordProgress();
    assertGameStarted(returnedData, SINGLEPLAYER);
    assertThat(returnedData.getWordProgress()).contains(" _ ");
    assertThat(Character.isLetter(wordProgress.charAt(0)));
    assertThat(Character.isLetter(wordProgress.charAt(wordProgress.length() - 1)));
    assertThat(returnedData.getWord().length()).isGreaterThanOrEqualTo(3);
  }

  @Test
  public void startsMultiplayerGameCorrectly()
      throws InvalidWordException, InvalidCategoryException {
    NewGameDTO newGameDTO = new NewGameDTO("Alfa Romeo", "MULTIPLAYER", "CARS");

    GameData returnedData = gameService.startNewGame(newGameDTO, session);

    String wordProgress = returnedData.getWordProgress();
    assertGameStarted(returnedData, MULTIPLAYER);
    assertThat(returnedData.getIrrelevantCharacters()).isEqualTo(1);
    assertThat(returnedData.getWord()).isEqualTo("ALFA ROMEO");
    assertThat(wordProgress).isEqualTo("A _ _ A   _ O _ _ O");
  }

  @Test
  public void makesCorrectGuess() throws InvalidGuessException, GameNotFoundException {
    GameData game = new GameData("OPEL", "CARS", MULTIPLAYER, MAX_LIVES);
    game.getUnguessedLetters().removeAll(Arrays.asList('O', 'L'));
    game.getRightGuesses().addAll(Arrays.asList('O', 'L'));

    gameService.makeTry(game, "p", session);

    assertThat(game.getWordProgress()).isEqualTo("O P _ L");
    assertThat(game.getLives()).isEqualTo(MAX_LIVES);
    assertThat(game.getGuessesMade()).isEqualTo(1);
    assertThat(game.getRightGuesses().size()).isEqualTo(3);
    assertTrue(game.getRightGuesses().contains('P'));
    assertFalse(game.getUnguessedLetters().contains('P'));
  }

  @Test
  public void rightGuessSavesWinningGame() throws InvalidGuessException, GameNotFoundException {
    List<GameData> games = new ArrayList<GameData>();
    GameData game = new GameData("OPEL", "CARS", MULTIPLAYER, MAX_LIVES);
    game.getUnguessedLetters().removeAll(Arrays.asList('O', 'P', 'L'));
    game.getRightGuesses().addAll(Arrays.asList('O', 'P', 'L'));

    gameService.makeTry(game, "e", session);

    assertTrue(game.isFinished());
    assertTrue(game.isGameWon());
  }

  @Test
  public void wrongGuessSavesLosingGame() throws InvalidGuessException, GameNotFoundException {
    GameData game = new GameData("OPEL", "CARS", MULTIPLAYER, 1);

    gameService.makeTry(game, "J", session);

    assertTrue(game.isFinished());
    assertFalse(game.isGameWon());
  }

  private void assertGameStarted(GameData game, GamemodeEnum gamemodeEnum) {
    assertThat(game.getCategory()).isEqualTo("CARS");
    assertThat(game.getGamemode()).isEqualTo(gamemodeEnum);
    assertThat(game.getLives()).isEqualTo(MAX_LIVES);
    assertThat(game.getRightGuesses()).isNotEmpty();
  }

  @Test
  public void throwsExceptionWhenGuessIsInvalid()
      throws InvalidWordException, InvalidCategoryException {
    GameData game = new GameData("TOYOTA", "CARS", MULTIPLAYER, MAX_LIVES);

    assertThatThrownBy(
            () -> {
              gameService.makeTry(game, "#", session);
            })
        .isInstanceOf(InvalidGuessException.class)
        .hasMessageContaining("Your guess was invalid. It should be a letter.");
  }

  @Test
  public void returnsNullWhenGameNotFound() throws GameNotFoundException {
    List<GameData> games = new ArrayList<GameData>();
    when(session.getAttribute(PREVIOUS_GAMES)).thenReturn(games);

    assertThatThrownBy(
            () -> {
              gameService.getGame(1000L, session);
            })
        .isInstanceOf(GameNotFoundException.class)
        .hasMessageContaining("This game does not exist.");
  }

  @Test
  public void returnsCorrectGame() throws GameNotFoundException {
    List<GameData> games = new ArrayList<GameData>();
    GameData game = new GameData();
    games.add(game);
    when(session.getAttribute(PREVIOUS_GAMES)).thenReturn(games);

    GameData returnedData = gameService.getGame(game.getId(), session);

    assertThat(returnedData).isEqualTo(game);
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

    List<GameData> returnedGames = gameService.getAllFinishedGames(session);

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

    List<GameData> returnedGames = gameService.getAllOngoingGames(session);

    assertThat(returnedGames).isEqualTo(Arrays.asList(game1));
  }
}
