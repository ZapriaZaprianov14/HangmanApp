package com.proxiad.trainee.unit;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import com.proxiad.trainee.GameData;
import com.proxiad.trainee.NewGameDTO;
import com.proxiad.trainee.controllers.GameController;
import com.proxiad.trainee.exceptions.*;
import com.proxiad.trainee.interfaces.GameService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

public class GameControllerTests {

  @Mock private GameService gameService;

  @InjectMocks private GameController gameController;

  private MockMvc mockMvc;

  private static final String CONTROLLER_MAPPING = "/api/v1/games";

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
    mockMvc = MockMvcBuilders.standaloneSetup(gameController).build();
  }

  @Test
  public void testStartNewSingleplayerGame() throws Exception {
    GameData game = new GameData();
    game.setId(0L);
    when(gameService.startNewGame(any(NewGameDTO.class), any(MockHttpSession.class)))
        .thenReturn(game);
    mockMvc
        .perform(post(CONTROLLER_MAPPING + "/singleplayer/category/{category}", "CARS"))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl(CONTROLLER_MAPPING + "/0"));
  }

  @Test
  public void testStartNewMultiplayerGameWithWordRevealed() throws Exception {
    NewGameDTO newGameDTO = new NewGameDTO("aaaabbbb", "MULTIPLAYER", "CARS");

    mockMvc
        .perform(post(CONTROLLER_MAPPING + "/multiplayer").flashAttr("newGameDTO", newGameDTO))
        .andExpect(status().isOk())
        .andExpect(view().name("multiplayer-input-view"))
        .andExpect(model().attributeHasFieldErrors("newGameDTO", "wordToGuess"));
  }

  @Test
  public void testStartNewMultiplayerWithStringsEndingInSpecialCharacters() throws Exception {
    testStartMultiplayerGameWithErrors("word-", "category-");
  }

  @Test
  public void testStartNewMultiplayerWithStringsBeginingWithSpecialCharacters() throws Exception {
    testStartMultiplayerGameWithErrors("-word", "-category");
  }

  @Test
  public void testStartNewMultiplayerWithStringsHavingSpecialCharacters() throws Exception {
    testStartMultiplayerGameWithErrors("wo1rd", "cate2gory");
  }

  @Test
  public void testStartNewMultiplayerWithShortStrings() throws Exception {
    testStartMultiplayerGameWithErrors("wo", "ca");
  }

  private void testStartMultiplayerGameWithErrors(String invalidWord, String invalidCategory)
      throws Exception {
    NewGameDTO newGameDTO = new NewGameDTO(invalidWord, "MULTIPLAYER ", invalidCategory);

    mockMvc
        .perform(post(CONTROLLER_MAPPING + "/multiplayer").flashAttr("newGameDTO", newGameDTO))
        .andExpect(status().isOk())
        .andExpect(view().name("multiplayer-input-view"))
        .andExpect(model().attributeHasFieldErrors("newGameDTO", "wordToGuess", "category"));
  }

  @Test
  public void testStartNewMultiplayerGameSuccessfully() throws Exception {
    NewGameDTO newGameDTO = new NewGameDTO("word", "MULTIPLAYER", "CARS");
    GameData game = new GameData();
    game.setId(0L);
    when(gameService.startNewGame(any(NewGameDTO.class), any(MockHttpSession.class)))
        .thenReturn(game);
    mockMvc
        .perform(post(CONTROLLER_MAPPING + "/multiplayer").flashAttr("newGameDTO", newGameDTO))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl(CONTROLLER_MAPPING + "/" + game.getId()));

    verify(gameService).startNewGame(any(NewGameDTO.class), any(MockHttpSession.class));
  }

  @Test
  public void testReturnsMultiplayerInputPage() throws Exception {
    mockMvc
        .perform(get(CONTROLLER_MAPPING + "/multiplayer"))
        .andExpect(status().isOk())
        .andExpect(view().name("multiplayer-input-view"));
  }

  @Test
  public void testPlaysGuessCorrectly() throws Exception {
    GameData game = new GameData();
    when(gameService.getGame(any(Long.class), any(MockHttpSession.class))).thenReturn(game);
    when(gameService.makeTry(any(GameData.class), anyString(), any(MockHttpSession.class)))
        .thenReturn(game);

    mockMvc
        .perform(post(CONTROLLER_MAPPING + "/{gameId}/guess/{guess}", "0", "G"))
        .andExpect(status().isOk())
        .andExpect(view().name("game-view"));
  }

  @Test
  public void testReturnsWinPageWhenGameWon() throws Exception {
    GameData game = new GameData();
    game.setGameWon(true);
    game.setFinished(true);
    when(gameService.getGame(any(Long.class), any(MockHttpSession.class))).thenReturn(game);
    when(gameService.makeTry(any(GameData.class), anyString(), any(MockHttpSession.class)))
        .thenReturn(game);

    mockMvc
        .perform(post(CONTROLLER_MAPPING + "/{gameId}/guess/{guess}", "0", "G"))
        .andExpect(status().isOk())
        .andExpect(view().name("win-view"));
  }

  @Test
  public void testReturnsLossPageWhenGameLost() throws Exception {
    GameData game = new GameData();
    game.setGameWon(false);
    game.setFinished(true);
    when(gameService.getGame(any(Long.class), any(MockHttpSession.class))).thenReturn(game);
    when(gameService.makeTry(any(GameData.class), anyString(), any(MockHttpSession.class)))
        .thenReturn(game);

    mockMvc
        .perform(post(CONTROLLER_MAPPING + "/{gameId}/guess/{guess}", "0", "G"))
        .andExpect(status().isOk())
        .andExpect(view().name("loss-view"));
  }

  @Test
  public void testGetsGame() throws Exception {
    mockMvc
        .perform(get(CONTROLLER_MAPPING + "/{gameId}", "0"))
        .andExpect(status().isOk())
        .andExpect(view().name("game-view"));
  }

  @Test
  public void testGetWhenGameNotFound() throws Exception {
    mockMvc.perform(get(CONTROLLER_MAPPING + "/{gameId}", "0d")).andExpect(status().isBadRequest());
  }

  @Test
  public void testRedirectGetGameWithGuess() throws Exception {
    mockMvc
        .perform(get(CONTROLLER_MAPPING + "/{gameId}/guess/{guess}", "0", "A"))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl(CONTROLLER_MAPPING + "/0"));
  }

  @Test
  public void testInvalidCategoryExceptionHandler() throws Exception {
    when(gameService.startNewGame(any(NewGameDTO.class), any(MockHttpSession.class)))
        .thenThrow(InvalidCategoryException.class);
    mockMvc
        .perform(post(CONTROLLER_MAPPING + "/singleplayer/category/{category}", "invalidCategory"))
        .andExpect(status().isBadRequest())
        .andExpect(view().name("bad-request-view"));
  }

  @Test
  public void returnsOngoingViewWithData() throws Exception {
    GameData game = new GameData();
    game.setFinished(false);
    List<GameData> games = Collections.singletonList(game);
    when(gameService.getAllOngoingGames(any(MockHttpSession.class))).thenReturn(games);
    mockMvc
        .perform(get(CONTROLLER_MAPPING + "/ongoing"))
        .andExpect(status().isOk())
        .andExpect(view().name("ongoing-view"))
        .andExpect(model().attribute("gamesReversed", games));
  }

  @Test
  public void returnsHistoryJSPWhenPresentFinishedGames() throws Exception {
    GameData game = new GameData();
    game.setFinished(true);
    List<GameData> games = Collections.singletonList(game);
    when(gameService.getAllFinishedGames(any(MockHttpSession.class))).thenReturn(games);
    mockMvc
        .perform(get(CONTROLLER_MAPPING + "/finished"))
        .andExpect(status().isOk())
        .andExpect(view().name("finished-view"))
        .andExpect(model().attribute("gamesReversed", games));
  }
}
