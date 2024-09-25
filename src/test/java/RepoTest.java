import static org.assertj.core.api.Assertions.assertThat;
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
import com.proxiad.trainee.CategoryEnum;
import com.proxiad.trainee.GameData;
import com.proxiad.trainee.GameRepository;
import com.proxiad.trainee.GamemodeEnum;
import jakarta.servlet.http.HttpSession;

public class RepoTest {

  @Mock HttpSession session;

  @Rule public MockitoRule rule = MockitoJUnit.rule();

  GameRepository repository;

  @Before
  public void setUp() {
    ApplicationContext context = new ClassPathXmlApplicationContext("ApplicationContext.xml");
    repository = context.getBean(GameRepository.class);
  }

  @Test
  public void repoReturnsNullWhenListEmpty() {
    when(session.getAttribute("previousGames")).thenReturn(null);
    // assertEquals(repository.getAllGames(session), null);
    assertThat(repository.getAllGames(session)).isEqualTo(null);
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
    assertThat(repository.getAllGames(session)).isEqualTo(games);
  }

  @Test
  public void returnsNullWhenGameNotFound() {
    GameData game1 = new GameData("BURGAS", CategoryEnum.CITIES, GamemodeEnum.MULTIPLAYER, 9);
    GameData game2 = new GameData("PLOVDIV", CategoryEnum.CITIES, GamemodeEnum.MULTIPLAYER, 9);
    List<GameData> games = new ArrayList<GameData>();
    games.add(game1);
    games.add(game2);
    when(session.getAttribute("previousGames")).thenReturn(games);
    GameData returnedData = repository.getGame(UUID.randomUUID(), session);
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
    GameData returnedData = repository.getGame(game1.getId(), session);
    // assertEquals(returnedData, null);
    assertThat(returnedData).isEqualTo(game1);
  }

  @Test
  public void returnsCurrentGame() {
    GameData game = new GameData("BURGAS", CategoryEnum.CITIES, GamemodeEnum.MULTIPLAYER, 9);
    when(session.getAttribute("currentGame")).thenReturn(game);
    GameData returnedGame = repository.getCurrentGame(session);
    // assertEquals(game, returnedGame);
    assertThat(game).isEqualTo(returnedGame);
  }

  @Test
  public void savesGameCorrectly() {
    GameData game = new GameData("BURGAS", CategoryEnum.CITIES, GamemodeEnum.MULTIPLAYER, 9);
    List<GameData> games = new ArrayList<>();
    when(session.getAttribute("previousGames")).thenReturn(games);

    repository.saveGame(game, session);

    verify(session).setAttribute(eq("previousGames"), anyList());
    assertThat(games).contains(game);
  }

  //  @Test
  //  public void savesGameCorrectlyWhenListIsNull() {
  //    GameData game = new GameData("BURGAS", CategoryEnum.CITIES, GamemodeEnum.MULTIPLAYER, 9);
  //    List<GameData> games = null;
  //    when(session.getAttribute("previousGames")).thenReturn(games);
  //
  //    repository.saveGame(game, session);
  //
  //    verify(session).setAttribute(eq("previousGames"), anyList());
  //    assertThat(games).contains(game);
  //  }

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

    repository.leaveGame(session);

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

    GameData returnedGame = repository.resumeGame(game1.getId(), session);

    verify(session).setAttribute("currentGame", game1);
    assertThat(returnedGame).isEqualTo(game1);
    assertThat(games).doesNotContain(game1);
  }
}
