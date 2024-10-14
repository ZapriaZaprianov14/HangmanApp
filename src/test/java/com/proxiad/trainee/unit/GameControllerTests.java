package com.proxiad.trainee.unit;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
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

  private static final String CONTROLLER_MAPPING = "/api/games";

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
    mockMvc = MockMvcBuilders.standaloneSetup(gameController).build();
  }

  @Test
  public void startNewSingleplayerGame() throws Exception {
    mockMvc
        .perform(post(CONTROLLER_MAPPING + "/singleplayer/category/{category}", "CARS"))
        .andExpect(status().isOk())
        .andExpect(view().name("game-view"));
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

    mockMvc
        .perform(post(CONTROLLER_MAPPING + "/multiplayer").flashAttr("newGameDTO", newGameDTO))
        .andExpect(status().isOk())
        .andExpect(view().name("game-view"));

    verify(gameService).startNewGame(any(NewGameDTO.class), any(MockHttpSession.class));
  }

  @Test
  public void returnsMultiplayerInputPage() throws Exception {
    mockMvc
        .perform(get(CONTROLLER_MAPPING + "/multiplayerInput"))
        .andExpect(status().isOk())
        .andExpect(view().name("multiplayer-input-view"));
  }

  @Test
  public void playsGuessCorrectly() throws Exception {
    GameData game = new GameData();
    when(gameService.getCurrentGame(any(MockHttpSession.class))).thenReturn(game);
    when(gameService.makeTry(any(GameData.class), anyString(), any(MockHttpSession.class)))
        .thenReturn(game);

    mockMvc
        .perform(post(CONTROLLER_MAPPING + "/guess/{guess}", "G"))
        .andExpect(status().isOk())
        .andExpect(view().name("game-view"));
  }

  @Test
  public void returnsWinPageWhenGameWon() throws Exception {
    GameData game = new GameData();
    game.setGameWon(true);
    game.setFinished(true);
    when(gameService.getCurrentGame(any(MockHttpSession.class))).thenReturn(game);
    when(gameService.makeTry(any(GameData.class), anyString(), any(MockHttpSession.class)))
        .thenReturn(game);

    mockMvc
        .perform(post(CONTROLLER_MAPPING + "/guess/{guess}", "G"))
        .andExpect(status().isOk())
        .andExpect(view().name("win-view"));
  }

  @Test
  public void returnsLossPageWhenGameLost() throws Exception {
    GameData game = new GameData();
    game.setGameWon(false);
    game.setFinished(true);
    when(gameService.getCurrentGame(any(MockHttpSession.class))).thenReturn(game);
    when(gameService.makeTry(any(GameData.class), anyString(), any(MockHttpSession.class)))
        .thenReturn(game);

    mockMvc
        .perform(post(CONTROLLER_MAPPING + "/guess/{guess}", "G"))
        .andExpect(status().isOk())
        .andExpect(view().name("loss-view"));
  }

  @Test
  public void resumesGame() throws Exception {
    when(gameService.resumeGame(any(UUID.class), any(MockHttpSession.class)))
        .thenReturn(new GameData());
    mockMvc
        .perform(
            post(CONTROLLER_MAPPING + "/{gameID}/resume", "123e4567-a89b-12d3-a456-426614174000"))
        .andExpect(status().isOk())
        .andExpect(view().name("game-view"));
  }

  @Test
  public void testResumeWhenGameNotFound() throws Exception {
    when(gameService.resumeGame(any(UUID.class), any(MockHttpSession.class)))
        .thenThrow(GameNotFoundException.class);

    mockMvc
        .perform(
            post(CONTROLLER_MAPPING + "/{gameId}/resume", "123e4567-a89b-12d3-a456-426614174000"))
        .andExpect(status().isBadRequest())
        .andExpect(view().name("bad-request-view"));
  }

  @Test
  public void testLeaveGame() throws Exception {
    mockMvc
        .perform(post(CONTROLLER_MAPPING + "/leave"))
        .andExpect(status().isOk())
        .andExpect(view().name("home-view"));
  }

  @Test
  public void testGetEndpoint() throws Exception {
    mockMvc
        .perform(get(CONTROLLER_MAPPING + "/singleplayer/category/{category}", "CARS"))
        .andExpect(status().isOk())
        .andExpect(view().name("game-view"));
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
}
