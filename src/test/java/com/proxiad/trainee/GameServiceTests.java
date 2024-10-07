// package com.proxiad.trainee;
//
// import static org.assertj.core.api.Assertions.assertThat;
// import static org.assertj.core.api.Assertions.assertThatThrownBy;
// import static org.junit.Assert.assertFalse;
// import static org.junit.Assert.assertTrue;
// import static org.mockito.ArgumentMatchers.anyList;
// import static org.mockito.ArgumentMatchers.booleanThat;
// import static org.mockito.ArgumentMatchers.eq;
// import static org.mockito.Mockito.never;
// import static org.mockito.Mockito.times;
// import static org.mockito.Mockito.verify;
// import static org.mockito.Mockito.when;
// import java.util.ArrayList;
// import java.util.Arrays;
// import java.util.HashSet;
// import java.util.List;
// import java.util.UUID;
// import org.junit.Before;
// import org.junit.Rule;
// import org.junit.Test;
// import org.mockito.Mock;
// import org.mockito.junit.MockitoJUnit;
// import org.mockito.junit.MockitoRule;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.context.ApplicationContext;
// import org.springframework.context.support.ClassPathXmlApplicationContext;
// import com.proxiad.trainee.enums.GamemodeEnum;
// import com.proxiad.trainee.exceptions.GameNotFoundException;
// import com.proxiad.trainee.exceptions.InvalidCategoryException;
// import com.proxiad.trainee.exceptions.InvalidGuessException;
// import com.proxiad.trainee.exceptions.InvalidWordException;
// import com.proxiad.trainee.interfaces.GameService;
// import static com.proxiad.trainee.enums.GamemodeEnum.*;
// import static com.proxiad.trainee.Constants.MAX_LIVES;
// import jakarta.servlet.http.HttpSession;
//
// public class GameServiceTests {
//  @Mock HttpSession session;
//
//  @Rule public MockitoRule rule = MockitoJUnit.rule();
//
//  @Autowired private GameService gameService;
//
//  @Test
//  public void startsSingleplayerGameCorrectly()
//      throws InvalidWordException, InvalidCategoryException {
//    String category = "CARS";
//    NewGameDTO newGameDTO = new NewGameDTO(null, "MULTIPLAYER", "CARS");
//    GameData returnedData = gameService.startNewGame(newGameDTO, session);
//
//    String wordProgress = returnedData.getWordProgress();
//
//    assertGameStarted(returnedData, SINGLEPLAYER);
//    assertThat(returnedData.getWordProgress()).contains(" _ ");
//    assertThat(Character.isLetter(wordProgress.charAt(0)));
//    assertThat(Character.isLetter(wordProgress.charAt(wordProgress.length() - 1)));
//    assertThat(returnedData.getWord().length()).isGreaterThanOrEqualTo(3);
//  }
//
//  @Test
//  public void startsMultiplayerGameCorrectly()
//      throws InvalidWordException, InvalidCategoryException {
//    String category = "CARS";
//    NewGameDTO newGameDTO = new NewGameDTO("Alfa Romeo", "MULTIPLAYER", "CARS");
//    GameData returnedData = gameService.startNewGame(newGameDTO, session);
//
//    String wordProgress = returnedData.getWordProgress();
//
//    assertGameStarted(returnedData, MULTIPLAYER);
//    assertThat(returnedData.getIrrelevantCharacters()).isEqualTo(1);
//    assertThat(returnedData.getWord()).isEqualTo("ALFA ROMEO");
//    assertThat(wordProgress).isEqualTo("A _ _ A   _ O _ _ O");
//  }
//
//  @Test
//  public void makesCorrectGuess() throws InvalidGuessException, GameNotFoundException {
//    GameData game = new GameData("OPEL", "CARS", MULTIPLAYER, MAX_LIVES);
//    game.getUnguessedLetters().removeAll(Arrays.asList('O', 'L'));
//    game.getRightGuesses().addAll(Arrays.asList('O', 'L'));
//
//    gameService.makeTry(game, "p", session);
//
//    assertThat(game.getWordProgress()).isEqualTo("O P _ L");
//    assertThat(game.getLives()).isEqualTo(MAX_LIVES);
//    assertThat(game.getGuessesMade()).isEqualTo(1);
//    assertThat(game.getRightGuesses().size()).isEqualTo(3);
//    assertTrue(game.getRightGuesses().contains('P'));
//    assertFalse(game.getUnguessedLetters().contains('P'));
//  }
//
//  @Test
//  public void rightGuessSavesWinningGame() throws InvalidGuessException, GameNotFoundException {
//    List<GameData> games = new ArrayList<GameData>();
//    GameData game = new GameData("OPEL", "CARS", MULTIPLAYER, MAX_LIVES);
//    game.getUnguessedLetters().removeAll(Arrays.asList('O', 'P', 'L'));
//    game.getRightGuesses().addAll(Arrays.asList('O', 'P', 'L'));
//    games.add(game);
//
//    gameService.makeTry(game, "e", session);
//
//    assertGameFinished(game, games, true);
//  }
//
//  @Test
//  public void wrongGuessSavesLosingGame() throws InvalidGuessException, GameNotFoundException {
//    List<GameData> games = new ArrayList<GameData>();
//    GameData game = new GameData("OPEL", "CARS", MULTIPLAYER, 1);
//    games.add(game);
//
//    gameService.makeTry(game, "J", session);
//
//    assertGameFinished(game, games, false);
//  }
//
//  private void assertGameFinished(GameData game, List<GameData> games, boolean expectedIsWon) {
//    assertThat(game.getWordProgress()).isEqualTo("OPEL");
//    assertTrue(game.isFinished());
//    assertThat(game.isGameWon()).isEqualTo(expectedIsWon);
//    assertTrue(games.contains(game));
//    verify(session, times(1)).setAttribute("previousGames", games);
//    // verify(session, never()).setAttribute("previousGames", games);
//  }
//
//  private void assertGameStarted(GameData game, GamemodeEnum gamemodeEnum) {
//    assertThat(game.getCategory()).isEqualTo("CARS");
//    assertThat(game.getGamemode()).isEqualTo(gamemodeEnum);
//    assertThat(game.getLives()).isEqualTo(MAX_LIVES);
//    assertThat(game.getRightGuesses()).isNotEmpty();
//  }
//
//  @Test
//  public void throwsExceptionWhenGuessIsInvalid()
//      throws InvalidWordException, InvalidCategoryException {
//    GameData game = new GameData("TOYOTA", "CARS", MULTIPLAYER, MAX_LIVES);
//
//    assertThatThrownBy(
//            () -> {
//              gameService.makeTry(game, "#", session);
//            })
//        .isInstanceOf(InvalidGuessException.class)
//        .hasMessageContaining("Your guess was invalid. The guess should be a letter.");
//  }
//
//  @Test
//  public void throwsExceptionWhenCategoryIsInvalid()
//      throws InvalidWordException, InvalidCategoryException {
//    NewGameDTO newGameDTO = new NewGameDTO("word", "MULTIPLAYER", "wrong2Category");
//    assertThatThrownBy(
//            () -> {
//              gameService.startNewGame(newGameDTO, session);
//            })
//        .isInstanceOf(InvalidCategoryException.class)
//        .hasMessageContaining("The given category is invalid.");
//  }
//
//  @Test
//  public void throwsExceptionWhenWordIsShort() {
//    NewGameDTO newGameDTO = new NewGameDTO("Al", "MULTIPLAYER", "CARS");
//    assertThatThrownBy(
//            () -> {
//              gameService.startNewGame(newGameDTO, session);
//            })
//        .isInstanceOf(InvalidWordException.class)
//        .hasMessageContaining("Word should have at least 3 characters");
//  }
//
//  @Test
//  public void throwsExceptionWhenWordDoesNotEndWithLetter() {
//    NewGameDTO newGameDTO = new NewGameDTO("Alfa ", "MULTIPLAYER", "CARS");
//    assertThatThrownBy(
//            () -> {
//              gameService.startNewGame(newGameDTO, session);
//            })
//        .isInstanceOf(InvalidWordException.class)
//        .hasMessageContaining("Word should end with a letter.");
//  }
//
//  @Test
//  public void throwsExceptionWhenWholeWordRevealed() {
//    NewGameDTO newGameDTO = new NewGameDTO("Aaabbb", "MULTIPLAYER", "CARS");
//    assertThatThrownBy(
//            () -> {
//              gameService.startNewGame(newGameDTO, session);
//            })
//        .isInstanceOf(InvalidWordException.class)
//        .hasMessageContaining(
//            "At least one of the letters should not be revealed at the beginning of the game");
//  }
//
//  @Test
//  public void throwsExceptionWhenWordHasSpecialCharacters() {
//    NewGameDTO newGameDTO = new NewGameDTO("Aaabbb", "MULTIPLAYER", "CARS");
//    assertThatThrownBy(
//            () -> {
//              gameService.startNewGame(newGameDTO, session);
//            })
//        .isInstanceOf(InvalidWordException.class)
//        .hasMessageContaining("Word should contain only letters, whitespaces or dashes.");
//  }
//
//  @Test
//  public void repoReturnsNullWhenListEmpty() {
//    when(session.getAttribute("previousGames")).thenReturn(null);
//    assertThat(gameService.getAllGames(session)).isEqualTo(null);
//  }
//
//  @Test
//  public void repoReturnsAllGames() {
//    List<GameData> games = new ArrayList<GameData>();
//
//    when(session.getAttribute("previousGames")).thenReturn(games);
//
//    assertThat(gameService.getAllGames(session)).isEqualTo(games);
//  }
//
//  @Test
//  public void returnsNullWhenGameNotFound() throws GameNotFoundException {
//    List<GameData> games = new ArrayList<GameData>();
//    when(session.getAttribute("previousGames")).thenReturn(games);
//
//    GameData returnedData = gameService.getGame(UUID.randomUUID(), session);
//
//    assertThat(returnedData).isEqualTo(null);
//  }
//
//  @Test
//  public void returnsCorrectGame() throws GameNotFoundException {
//    List<GameData> games = new ArrayList<GameData>();
//    GameData game = new GameData();
//    games.add(game);
//    when(session.getAttribute("previousGames")).thenReturn(games);
//
//    GameData returnedData = gameService.getGame(game.getId(), session);
//
//    assertThat(returnedData).isEqualTo(game);
//  }
//
//  @Test
//  public void returnsCurrentGame() throws GameNotFoundException {
//    GameData game = new GameData();
//    when(session.getAttribute("currentGame")).thenReturn(game);
//
//    GameData returnedGame = gameService.getCurrentGame(session);
//
//    assertThat(game).isEqualTo(returnedGame);
//  }
//
//  @Test
//  public void leavesGame() throws GameNotFoundException {
//    GameData game = new GameData();
//    List<GameData> games = new ArrayList<GameData>();
//    when(session.getAttribute("currentGame")).thenReturn(game);
//    when(session.getAttribute("previousGames")).thenReturn(games);
//
//    gameService.leaveGame(session);
//
//    verify(session).removeAttribute(eq("currentGame"));
//    assertThat(games).contains(game);
//  }
//
//  @Test
//  public void resumesGame() throws GameNotFoundException {
//    GameData game = new GameData();
//    List<GameData> games = new ArrayList<GameData>();
//    games.add(game);
//    when(session.getAttribute("previousGames")).thenReturn(games);
//
//    GameData returnedGame = gameService.resumeGame(game.getId(), session);
//
//    verify(session).setAttribute("currentGame", game);
//    assertThat(returnedGame).isEqualTo(game);
//    assertThat(games).doesNotContain(game);
//  }
// }
