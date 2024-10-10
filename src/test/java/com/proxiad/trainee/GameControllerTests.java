package com.proxiad.trainee;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import java.util.UUID;
import com.proxiad.trainee.exceptions.*;
import com.proxiad.trainee.interfaces.GameService;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

public class GameControllerTests {

  @Mock private GameService gameService;

  @Mock private HttpSession session;

  @Mock private Model model;

  @Mock private BindingResult bindingResult;

  @InjectMocks private GameController gameController;

  private MockMvc mockMvc;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
    mockMvc = MockMvcBuilders.standaloneSetup(gameController).build();
  }

  @Test
  public void testStartNewSingleplayerGame() throws Exception {
    mockMvc
        .perform(post("/games/game/{category}", "CARS"))
        .andExpect(status().isOk())
        .andExpect(view().name("game-view"));
  }

  @Test
  public void testStartNewMultiplayerGameWithErrors() throws Exception {
    NewGameDTO newGameDTO = new NewGameDTO("aaaabbbb", "MULTIPLAYER", "CARS");
    // when(bindingResult.hasErrors()).thenReturn(true);

    mockMvc
        .perform(post("/game/multiplayer").flashAttr("newGameDTO", newGameDTO))
        .andExpect(status().isOk())
        .andExpect(view().name("multiplayer-input-view"));
  }

  @Test
  public void testStartNewMultiplayerGameSuccessfully() throws Exception {
    NewGameDTO newGameDTO = new NewGameDTO("word", "MULTIPLAYER", "CARS");
    // when(bindingResult.hasErrors()).thenReturn(false);

    mockMvc
        .perform(post("/game/multiplayer").flashAttr("newGameDTO", newGameDTO))
        .andExpect(status().isOk())
        .andExpect(view().name("game-view"));

    verify(gameService).startNewGame(newGameDTO, session);
  }

  @Test
  public void testShowMultiplayerForm() throws Exception {
    mockMvc
        .perform(get("/games/multiplayerInput"))
        .andExpect(status().isOk())
        .andExpect(view().name("multiplayer-input-view"));
  }

  @Test
  public void testPlayGuess() throws Exception {
    GameData game = new GameData();
    when(gameService.getCurrentGame(any(HttpSession.class))).thenReturn(game);
    when(gameService.makeTry(any(GameData.class), anyString(), any(HttpSession.class)))
        .thenReturn(game);

    mockMvc
        .perform(post("/games/guess/{guess}", "G").sessionAttr("session", session))
        .andExpect(status().isOk())
        .andExpect(view().name("game-view"));
  }

  @Test
  public void testResumeGame() throws Exception {
    mockMvc
        .perform(post("/games/{gameID}/resume", "123e4567-a89b-12d3-a456-426614174000"))
        .andExpect(status().isOk())
        .andExpect(view().name("game-view"));
  }

  @Test
  public void testResumeGameWhenGameNotFound() throws Exception {
    when(gameService.resumeGame(any(UUID.class), any(HttpSession.class)))
        .thenThrow(GameNotFoundException.class);

    mockMvc
        .perform(post("/games/{gameId}/resume", "123e4567-a89b-12d3-a456-426614174000"))
        .andExpect(status().isBadRequest())
        .andExpect(view().name("bad-request-view"));
  }

  @Test
  public void testLeaveGame() throws Exception {
    mockMvc
        .perform(post("/games/leave").sessionAttr("session", session))
        .andExpect(status().isOk())
        .andExpect(view().name("home-view"));
  }

  @Test
  public void testGetHomeJSP() throws Exception {
    mockMvc
        .perform(get("/games/leave"))
        .andExpect(status().isOk())
        .andExpect(view().name("home-view"));
  }

  @Test
  public void testHandleGameRequests() throws Exception {
    when(gameService.getCurrentGame(session)).thenReturn(new GameData());

    mockMvc
        .perform(get("/games/game/category").sessionAttr("session", session))
        .andExpect(status().isOk())
        .andExpect(view().name("game-view"));
  }

  @Test
  public void testInvalidCategoryExceptionHandler() throws Exception {

    when(gameService.startNewGame(any(), any())).thenThrow(InvalidCategoryException.class);

    mockMvc
        .perform(post("/games/game/invalidCategory").sessionAttr("session", session))
        .andExpect(status().isBadRequest())
        .andExpect(view().name("bad-request-view"));
  }
}
