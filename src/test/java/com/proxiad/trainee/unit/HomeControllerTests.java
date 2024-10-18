package com.proxiad.trainee.unit;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;
import com.proxiad.trainee.GameData;
import com.proxiad.trainee.NewGameDTO;
import com.proxiad.trainee.controllers.HomeController;
import com.proxiad.trainee.interfaces.GameRepository;
import jakarta.servlet.http.HttpSession;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

public class HomeControllerTests {

  @Mock private Model model;

  @Mock private GameRepository repository;

  @InjectMocks private HomeController homeController;

  MockMvc mockMvc;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
    mockMvc = MockMvcBuilders.standaloneSetup(homeController).build();
  }

  @Test
  public void returnsHomeJSP() throws Exception {
    mockMvc
        .perform(get("/api/home"))
        .andExpect(status().isOk())
        .andExpect(view().name("home-view"));
  }

  @Test
  public void returnsOngoingViewWithData() throws Exception {
    GameData game = new GameData();
    game.setFinished(false);
    List<GameData> games = Collections.singletonList(game);
    when(repository.getAllOngoingGames(any(MockHttpSession.class))).thenReturn(games);
    mockMvc
        .perform(get("/api/ongoing"))
        .andExpect(status().isOk())
        .andExpect(view().name("ongoing-view"))
        .andExpect(model().attribute("gamesReversed", games));
  }

  @Test
  public void returnsHistoryJSPWhenPresentFinishedGames() throws Exception {
    GameData game = new GameData();
    game.setFinished(true);
    List<GameData> games = Collections.singletonList(game);
    when(repository.getAllFinishedGames(any(MockHttpSession.class))).thenReturn(games);
    mockMvc
        .perform(get("/api/history"))
        .andExpect(status().isOk())
        .andExpect(view().name("history-view"))
        .andExpect(model().attribute("gamesReversed", games));
  }
}
