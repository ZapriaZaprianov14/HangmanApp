package com.proxiad.trainee;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.ArrayList;
import java.util.Arrays;
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
import static com.proxiad.trainee.CategoryEnum.*;
import com.proxiad.trainee.Constants;
import com.proxiad.trainee.GameData;
import com.proxiad.trainee.GameService;
import static com.proxiad.trainee.GamemodeEnum.*;
import com.proxiad.trainee.InvalidWordException;
import jakarta.servlet.http.HttpSession;

public class GameServiceTests {
  @Mock HttpSession session;

  @Rule public MockitoRule rule = MockitoJUnit.rule();

  private GameService gameService;

  @Before
  public void setUp() {
    ApplicationContext context = new ClassPathXmlApplicationContext("ApplicationContext.xml");
    gameService = context.getBean(GameService.class);
  }

  private List<GameData> createGameDataList() {
    GameData game1 = new GameData();
    GameData game2 = new GameData();
    List<GameData> games = new ArrayList<GameData>();
    games.add(game1);
    games.add(game2);
    return games;
  }

  @Test
  public void startsSingleplayerGameCorrectly() throws InvalidWordException {
    String category = "CARS";
    GameData returnedData = gameService.startNewGame(category, null, session);

    String wordProgress = returnedData.getWordProgress();

    assertThat(returnedData.getCategory()).isEqualTo(CARS);
    assertThat(returnedData.getGamemode()).isEqualTo(SINGLEPLAYER);
    assertThat(returnedData.getLives()).isEqualTo(Constants.MAX_LIVES);
    assertThat(returnedData.getRightGuesses()).isNotEmpty();
    assertThat(returnedData.getWordProgress()).contains(" _ ");
    assertThat(Character.isLetter(wordProgress.charAt(0)));
    assertThat(Character.isLetter(wordProgress.charAt(wordProgress.length() - 1)));
    assertThat(returnedData.getWord().length()).isGreaterThanOrEqualTo(3);
  }

  @Test
  public void startsMultiplayerGameCorrectly() throws InvalidWordException {
    String category = "CARS";
    GameData returnedData = gameService.startNewGame(category, "Alfa Romeo", session);

    String wordProgress = returnedData.getWordProgress();

    assertThat(returnedData.getCategory()).isEqualTo(CARS);
    assertThat(returnedData.getGamemode()).isEqualTo(MULTIPLAYER);
    assertThat(returnedData.getLives()).isEqualTo(Constants.MAX_LIVES);
    assertThat(returnedData.getRightGuesses()).isNotEmpty();
    assertThat(returnedData.getIrrelevantCharacters()).isEqualTo(1);
    assertThat(returnedData.getWord()).isEqualTo("ALFA ROMEO");
    assertThat(wordProgress).isEqualTo("A _ _ A   _ O _ _ O");
  }

  @Test
  public void makesCorrectGuess() throws InvalidWordException {
    String category = "CARS";
    GameData returnedGame;

    returnedGame = gameService.startNewGame(category, "Opel", session);

    gameService.makeTry(returnedGame, "P", session);
    assertThat(returnedGame.getWordProgress()).isEqualTo("O P _ L");
    assertThat(returnedGame.getLives()).isEqualTo(Constants.MAX_LIVES);
    assertThat(returnedGame.getGuessesMade()).isEqualTo(1);
    assertThat(returnedGame.getRightGuesses().size()).isEqualTo(3);
    assertThat(returnedGame.getRightGuesses().contains('P'));
    assertThat(!returnedGame.getUnguessedLetters().contains('P'));
  }

  @Test
  public void rightGuessSavesWinningGame() throws InvalidWordException {
    String category = "CARS";
    List<GameData> games = new ArrayList<GameData>();
    when(session.getAttribute("previousGames")).thenReturn(games);

    GameData returnedGame = gameService.startNewGame(category, "Opel", session);

    gameService.makeTry(returnedGame, "P", session);
    gameService.makeTry(returnedGame, "E", session);
    assertThat(returnedGame.getWordProgress()).isEqualTo("OPEL");
    assertThat(returnedGame.isFinished());
    assertThat(returnedGame.isGameWon());
    verify(session).setAttribute("previousGames", games);
    assertThat(games).contains(returnedGame);
  }

  @Test
  public void worngGuessSavesLosingGame() throws InvalidWordException {
    String category = "CARS";
    GameData returnedGame;
    List<GameData> games = new ArrayList<GameData>();
    when(session.getAttribute("previousGames")).thenReturn(games);

    returnedGame = gameService.startNewGame(category, "Opel", session);

    gameService.makeTry(returnedGame, "Z", session);
    gameService.makeTry(returnedGame, "X", session);
    gameService.makeTry(returnedGame, "C", session);
    gameService.makeTry(returnedGame, "V", session);
    gameService.makeTry(returnedGame, "B", session);
    gameService.makeTry(returnedGame, "N", session);
    gameService.makeTry(returnedGame, "M", session);
    gameService.makeTry(returnedGame, "K", session);
    gameService.makeTry(returnedGame, "J", session);
    assertThat(returnedGame.getWordProgress()).isEqualTo("OPEL");
    assertThat(returnedGame.isFinished());
    assertThat(!returnedGame.isGameWon());
    assertThat(returnedGame.getGuessesMade()).isEqualTo(9);
    verify(session).setAttribute("previousGames", games);
    assertThat(games).contains(returnedGame);
  }

  @Test
  public void throwsExceptionWhenWordIsShort() {
    String category = "CARS";
    String word = "Al";
    assertThatThrownBy(
            () -> {
              gameService.startNewGame(category, word, session);
            })
        .isInstanceOf(InvalidWordException.class)
        .hasMessageContaining("Word should have at least 3 characters");
  }

  @Test
  public void throwsExceptionWhenWordDoesNotEndWithLetter() {
    String category = "CARS";
    String word = "Alfa#";
    assertThatThrownBy(
            () -> {
              gameService.startNewGame(category, word, session);
            })
        .isInstanceOf(InvalidWordException.class)
        .hasMessageContaining("Word should end with a letter.");
  }

  @Test
  public void throwsExceptionWhenWholeWordRevealed() {
    String category = "CARS";
    String word = "Aaabbb";
    assertThatThrownBy(
            () -> {
              gameService.startNewGame(category, word, session);
            })
        .isInstanceOf(InvalidWordException.class)
        .hasMessageContaining(
            "At least one of the letters should not be revealed at the beginning of the game");
  }

  @Test
  public void throwsExceptionWhenWordHasSpecialCharacters() {
    String category = "CARS";
    String word = "Aaa#bbb";
    assertThatThrownBy(
            () -> {
              gameService.startNewGame(category, word, session);
            })
        .isInstanceOf(InvalidWordException.class)
        .hasMessageContaining("Word should contain only letters, whitespaces or dashes.");
  }

  @Test
  public void repoReturnsNullWhenListEmpty() {
    when(session.getAttribute("previousGames")).thenReturn(null);
    assertThat(gameService.getAllGames(session)).isEqualTo(null);
  }

  @Test
  public void repoDoesReturnsAllGames() {
    List<GameData> games = createGameDataList();
    when(session.getAttribute("previousGames")).thenReturn(games);

    assertThat(gameService.getAllGames(session)).isEqualTo(games);
  }

  @Test
  public void returnsNullWhenGameNotFound() {
    List<GameData> games = createGameDataList();
    when(session.getAttribute("previousGames")).thenReturn(games);

    GameData returnedData = gameService.getGame(UUID.randomUUID(), session);

    assertThat(returnedData).isEqualTo(null);
  }

  @Test
  public void returnsCorrectGame() {
    List<GameData> games = createGameDataList();
    GameData game = games.get(0);
    when(session.getAttribute("previousGames")).thenReturn(games);

    GameData returnedData = gameService.getGame(game.getId(), session);

    assertThat(returnedData).isEqualTo(game);
  }

  @Test
  public void returnsCurrentGame() {
    GameData game = new GameData();
    when(session.getAttribute("currentGame")).thenReturn(game);

    GameData returnedGame = gameService.getCurrentGame(session);

    assertThat(game).isEqualTo(returnedGame);
  }

  @Test
  public void leavesGame() {
    List<GameData> games = createGameDataList();
    GameData game = games.get(0);
    when(session.getAttribute("currentGame")).thenReturn(game);
    when(session.getAttribute("previousGames")).thenReturn(games);

    gameService.leaveGame(session);

    verify(session).removeAttribute(eq("currentGame"));
    assertThat(games).contains(game);
  }

  @Test
  public void resumesGame() {
    List<GameData> games = createGameDataList();
    GameData game = games.get(0);
    when(session.getAttribute("previousGames")).thenReturn(games);

    GameData returnedGame = gameService.resumeGame(game.getId(), session);

    verify(session).setAttribute("currentGame", game);
    assertThat(returnedGame).isEqualTo(game);
    assertThat(games).doesNotContain(game);
  }

  @Test
  public void reversesListAndChecksIfItContainsOngoingGames() {
    //    GameData game1 = new GameData("BURGAS", CITIES, MULTIPLAYER, 9);
    //    game1.setFinished(false);
    //    GameData game2 = new GameData("PLOVDIV", CITIES, MULTIPLAYER, 9);
    //    game2.setFinished(true);
    //    List<GameData> games = new ArrayList<GameData>();
    //    games.add(game1);
    //    games.add(game2);
    //    List<GameData> reversedList = new ArrayList<GameData>();
    //    reversedList.add(game2);
    //    reversedList.add(game1);
    List<GameData> games = createGameDataList();
    games.get(0).setFinished(false);
    games.get(1).setFinished(true);
    List<GameData> reversedGames = Arrays.asList(games.get(1), games.get(0));

    games = gameService.reverseListOfGames(games);

    assertThat(gameService.containsFinishedGames(games));
    assertThat(gameService.containsOngoingGames(games));
    assertThat(games).isEqualTo(reversedGames);
  }
}
