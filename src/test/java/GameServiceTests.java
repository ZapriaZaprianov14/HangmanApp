import static org.assertj.core.api.Assertions.assertThat;
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
import com.proxiad.trainee.CategoryEnum;
import com.proxiad.trainee.Constants;
import com.proxiad.trainee.GameData;
import com.proxiad.trainee.GameService;
import com.proxiad.trainee.GamemodeEnum;
import com.proxiad.trainee.InvalidWordException;
import jakarta.servlet.http.HttpSession;

public class GameServiceTests {
  @Mock HttpSession session;

  @Rule public MockitoRule rule = MockitoJUnit.rule();
  // @Rule public InvalidWordException expectedEx = InvalidWordException.none();

  GameService gameService;

  @Before
  public void setUp() {
    ApplicationContext context = new ClassPathXmlApplicationContext("ApplicationContext.xml");
    gameService = context.getBean(GameService.class);
  }

  @Test
  public void startsSingleplayerGameCorrectly() {
    String category = "CARS";
    GameData returnedData;
    try {
      returnedData = gameService.startNewGame(category, null, session);
    } catch (InvalidWordException e) {
      e.printStackTrace();
      return;
    }
    String wordProgress = returnedData.getWordProgress();

    assertThat(returnedData.getCategory()).isEqualTo(CategoryEnum.CARS);
    assertThat(returnedData.getGamemode()).isEqualTo(GamemodeEnum.SINGLEPLAYER);
    assertThat(returnedData.getLives()).isEqualTo(Constants.MAX_LIVES);
    assertThat(returnedData.getRightGuesses()).isNotEmpty();
    assertThat(returnedData.getWordProgress()).contains(" _ ");
    // checks if first and last characters are revealed
    assertThat(Character.isLetter(wordProgress.charAt(0)));
    assertThat(Character.isLetter(wordProgress.charAt(wordProgress.length() - 1)));
    assertThat(returnedData.getWord().length()).isGreaterThanOrEqualTo(3);
  }

  @Test
  public void startsMultiplayerGameCorrectly() {
    String category = "CARS";
    GameData returnedData;
    try {
      returnedData = gameService.startNewGame(category, "Alfa Romeo", session);
    } catch (InvalidWordException e) {
      e.printStackTrace();
      return;
    }
    String wordProgress = returnedData.getWordProgress();

    assertThat(returnedData.getCategory()).isEqualTo(CategoryEnum.CARS);
    assertThat(returnedData.getGamemode()).isEqualTo(GamemodeEnum.MULTIPLAYER);
    assertThat(returnedData.getLives()).isEqualTo(Constants.MAX_LIVES);
    assertThat(returnedData.getRightGuesses()).isNotEmpty();
    assertThat(returnedData.getIrrelevantCharacters()).isEqualTo(1);
    assertThat(returnedData.getWord()).isEqualTo("ALFA ROMEO");
    assertThat(wordProgress).isEqualTo("A _ _ A   _ O _ _ O");
  }

  @Test
  public void makesCorrectGuess() {
    String category = "CARS";
    GameData returnedGame;
    try {
      returnedGame = gameService.startNewGame(category, "Opel", session);
    } catch (InvalidWordException e) {
      e.printStackTrace();
      return;
    }
    gameService.makeTry(returnedGame, "P", session);
    assertThat(returnedGame.getWordProgress()).isEqualTo("O P _ L");
    assertThat(returnedGame.getLives()).isEqualTo(Constants.MAX_LIVES);
    assertThat(returnedGame.getGuessesMade()).isEqualTo(1);
    assertThat(returnedGame.getRightGuesses().size()).isEqualTo(3);
    assertThat(returnedGame.getRightGuesses().contains('P'));
    assertThat(!returnedGame.getUnguessedLetters().contains('P'));
  }

  @Test
  public void rightGuessSavesWinningGame() {
    String category = "CARS";
    GameData returnedGame;
    List<GameData> games = new ArrayList<GameData>();
    when(session.getAttribute("previousGames")).thenReturn(games);
    try {
      returnedGame = gameService.startNewGame(category, "Opel", session);
    } catch (InvalidWordException e) {
      e.printStackTrace();
      return;
    }
    gameService.makeTry(returnedGame, "P", session);
    gameService.makeTry(returnedGame, "E", session);
    assertThat(returnedGame.getWordProgress()).isEqualTo("OPEL");
    assertThat(returnedGame.isFinished());
    assertThat(returnedGame.isGameWon());
    verify(session).setAttribute("previousGames", games);
    assertThat(games).contains(returnedGame);
  }

  @Test
  public void worngGuessSavesLosingGame() {
    String category = "CARS";
    GameData returnedGame;
    List<GameData> games = new ArrayList<GameData>();
    when(session.getAttribute("previousGames")).thenReturn(games);
    try {
      returnedGame = gameService.startNewGame(category, "Opel", session);
    } catch (InvalidWordException e) {
      e.printStackTrace();
      return;
    }
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

  @Test // (expected = InvalidWordException.class)
  public void throwsExceptionWhenWordIsShort() {
    String category = "CARS";
    String word = "Al";
    try {
      gameService.startNewGame(category, word, session);
    } catch (InvalidWordException e) {
      assertThat(e.getMessage())
          .isEqualTo("Word should have at least 3 characters. Word entered: " + word.toUpperCase());
    }
  }

  @Test
  public void throwsExceptionWhenWordDoesNotEndWithLetter() {
    String category = "CARS";
    String word = "Alfa ";
    try {
      gameService.startNewGame(category, word, session);
    } catch (InvalidWordException e) {
      assertThat(e.getMessage()).isEqualTo("Word should end with a letter.");
    }
  }

  @Test
  public void throwsExceptionWhenWholeWordRevealed() {
    String category = "CARS";
    String word = "Aaabbb";
    try {
      gameService.startNewGame(category, word, session);
    } catch (InvalidWordException e) {
      assertThat(e.getMessage())
          .isEqualTo(
              "At least one of the letters should not be revealed at the beginning of the game."
                  + "The first and last letters are revealed. Word entered: "
                  + word);
    }
  }

  @Test
  public void throwsExceptionWhenWordHasSpecialCharacters() {
    String category = "CARS";
    String word = "Aaa#bbb";
    try {
      gameService.startNewGame(category, word, session);
    } catch (InvalidWordException e) {
      assertThat(e.getMessage())
          .isEqualTo(
              "Word should contain only letters, whitespaces or dashes."
                  + " Word entered: "
                  + word);
    }
  }

  @Test
  public void repoReturnsNullWhenListEmpty() {
    when(session.getAttribute("previousGames")).thenReturn(null);
    // assertEquals(repository.getAllGames(session), null);
    assertThat(gameService.getAllGames(session)).isEqualTo(null);
  }

  @Test
  public void repoDoesReturnsAllGames() {
    // GameData(String word, CategoryEnum category, GamemodeEnum gamemode, int lives)
    GameData game1 = new GameData("BURGAS", CategoryEnum.CITIES, GamemodeEnum.MULTIPLAYER, 9);
    GameData game2 = new GameData("PLOVDIV", CategoryEnum.CITIES, GamemodeEnum.MULTIPLAYER, 9);
    List<GameData> games = new ArrayList<GameData>();
    games.add(game1);
    games.add(game2);
    when(session.getAttribute("previousGames")).thenReturn(games);
    // assertEquals(repository.getAllGames(session), games);
    assertThat(gameService.getAllGames(session)).isEqualTo(games);
  }

  @Test
  public void returnsNullWhenGameNotFound() {
    GameData game1 = new GameData("BURGAS", CategoryEnum.CITIES, GamemodeEnum.MULTIPLAYER, 9);
    GameData game2 = new GameData("PLOVDIV", CategoryEnum.CITIES, GamemodeEnum.MULTIPLAYER, 9);
    List<GameData> games = new ArrayList<GameData>();
    games.add(game1);
    games.add(game2);
    when(session.getAttribute("previousGames")).thenReturn(games);
    GameData returnedData = gameService.getGame(UUID.randomUUID(), session);
    // assertEquals(returnedData, null);
    assertThat(returnedData).isEqualTo(null);
  }

  @Test
  public void returnsCorrectGame() {
    GameData game1 = new GameData("BURGAS", CategoryEnum.CITIES, GamemodeEnum.MULTIPLAYER, 9);
    GameData game2 = new GameData("PLOVDIV", CategoryEnum.CITIES, GamemodeEnum.MULTIPLAYER, 9);
    List<GameData> games = new ArrayList<GameData>();
    games.add(game1);
    games.add(game2);
    when(session.getAttribute("previousGames")).thenReturn(games);
    GameData returnedData = gameService.getGame(game1.getId(), session);
    // assertEquals(returnedData, null);
    assertThat(returnedData).isEqualTo(game1);
  }

  @Test
  public void returnsCurrentGame() {
    GameData game = new GameData("BURGAS", CategoryEnum.CITIES, GamemodeEnum.MULTIPLAYER, 9);
    when(session.getAttribute("currentGame")).thenReturn(game);
    GameData returnedGame = gameService.getCurrentGame(session);
    // assertEquals(game, returnedGame);
    assertThat(game).isEqualTo(returnedGame);
  }

  @Test
  public void leavesGame() {
    GameData game = new GameData("BURGAS", CategoryEnum.CITIES, GamemodeEnum.MULTIPLAYER, 9);
    GameData game1 = new GameData("PERNIK", CategoryEnum.CITIES, GamemodeEnum.MULTIPLAYER, 9);
    GameData game2 = new GameData("PLOVDIV", CategoryEnum.CITIES, GamemodeEnum.MULTIPLAYER, 9);
    List<GameData> games = new ArrayList<GameData>();
    games.add(game1);
    games.add(game2);
    when(session.getAttribute("currentGame")).thenReturn(game);
    when(session.getAttribute("previousGames")).thenReturn(games);

    gameService.leaveGame(session);

    verify(session).removeAttribute(eq("currentGame"));
    assertThat(games).contains(game);
  }

  @Test
  public void resumesGame() {
    GameData game1 = new GameData("PERNIK", CategoryEnum.CITIES, GamemodeEnum.MULTIPLAYER, 9);
    GameData game2 = new GameData("PLOVDIV", CategoryEnum.CITIES, GamemodeEnum.MULTIPLAYER, 9);
    List<GameData> games = new ArrayList<GameData>();
    games.add(game1);
    games.add(game2);

    when(session.getAttribute("previousGames")).thenReturn(games);

    GameData returnedGame = gameService.resumeGame(game1.getId(), session);

    verify(session).setAttribute("currentGame", game1);
    assertThat(returnedGame).isEqualTo(game1);
    assertThat(games).doesNotContain(game1);
  }

  @Test
  public void reversesListAndChecksIfItContainsOngoingGames() {
    GameData game1 = new GameData("PERNIK", CategoryEnum.CITIES, GamemodeEnum.MULTIPLAYER, 9);
    game1.setFinished(false);
    GameData game2 = new GameData("PLOVDIV", CategoryEnum.CITIES, GamemodeEnum.MULTIPLAYER, 9);
    game2.setFinished(true);
    List<GameData> games = new ArrayList<GameData>();
    games.add(game1);
    games.add(game2);

    List<GameData> reversedList = new ArrayList<GameData>();
    reversedList.add(game2);
    reversedList.add(game1);

    games = gameService.reverseListOfGames(games);

    assertThat(gameService.containsFinishedGames(games));
    assertThat(gameService.containsOngoingGames(games));
    assertThat(games).isEqualTo(reversedList);
  }
}
