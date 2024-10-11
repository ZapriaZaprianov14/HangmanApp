package com.proxiad.trainee;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;
import com.proxiad.trainee.controllers.HomeController;
import jakarta.servlet.http.HttpSession;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

public class HomeControllerTests {

  @Mock private HttpSession session;

  @Mock private Model model;

  @InjectMocks private HomeController homeController;

  MockMvc mockMvc;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
    mockMvc = MockMvcBuilders.standaloneSetup(homeController).build();
  }

  @Test
  public void testHome() throws Exception {
    mockMvc.perform(get("/api/")).andExpect(status().isOk()).andExpect(view().name("home-view"));
  }

  @Test
  public void returnsEmptyJSPWhenNoOngoingGames() throws Exception {
    List<GameData> games = new ArrayList<GameData>();
    mockMvc
        .perform(get("/api/ongoing").sessionAttr("previousGames", games))
        .andExpect(status().isOk())
        .andExpect(view().name("empty-view"));
  }

  @Test
  public void returnsOngoingJSPWhenOngoingGames() throws Exception {
    List<GameData> games = new ArrayList<GameData>();
    GameData game = new GameData();
    game.setFinished(false);
    games.add(game);
    mockMvc
        .perform(get("/api/ongoing").sessionAttr("previousGames", games))
        .andExpect(status().isOk())
        .andExpect(view().name("ongoing-view"))
        .andExpect(model().attributeExists("gamesReversed"));
  }

  @Test
  public void returnsEmptyJSPWhenNoFinishedGames() throws Exception {
    List<GameData> games = new ArrayList<GameData>();
    mockMvc
        .perform(get("/api/history").sessionAttr("previousGames", games))
        .andExpect(status().isOk())
        .andExpect(view().name("empty-view"))
        .andExpect(model().attributeDoesNotExist("gamesReversed"));
  }

  @Test
  public void returnsHistoryJSPWhenFinishedGames() throws Exception {
    List<GameData> games = new ArrayList<GameData>();
    GameData game = new GameData();
    game.setFinished(true);
    games.add(game);
    mockMvc
        .perform(get("/api/history").sessionAttr("previousGames", games))
        .andExpect(status().isOk())
        .andExpect(view().name("history-view"))
        .andExpect(model().attributeExists("gamesReversed"));
  }
}
