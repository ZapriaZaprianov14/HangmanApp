package com.proxiad.trainee.controllers;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import static com.proxiad.trainee.Constants.PREVIOUS_GAMES;
import com.proxiad.trainee.GameData;

@Controller
@RequestMapping("/api")
@SessionAttributes(PREVIOUS_GAMES)
public class HomeController {

  @GetMapping()
  public String getHome() {
    return "home-view";
  }

  @GetMapping("/ongoing")
  public String getOngoing(@ModelAttribute(PREVIOUS_GAMES) List<GameData> games, Model model) {
    if (containsOngoingGames(games)) {
      model.addAttribute("gamesReversed", reverseListOfGames(games));
      return "ongoing-view";
    } else {
      model.addAttribute("message", "No ongoing games.");
      return "empty-view";
    }
  }

  @GetMapping("/history")
  public String getPrevious(@ModelAttribute(PREVIOUS_GAMES) List<GameData> games, Model model) {
    if (containsFinishedGames(games)) {
      model.addAttribute("gamesReversed", reverseListOfGames(games));
      return "history-view";
    } else {
      model.addAttribute("message", "No finished games.");
      return "empty-view";
    }
  }

  @ExceptionHandler(value = Exception.class)
  public String unexpectedException(Exception ex, Model model) {
    model.addAttribute("message", "Unexpected error");
    return "unexpected-view";
  }

  private boolean containsFinishedGames(List<GameData> games) {
    return games.stream().anyMatch(game -> game.isFinished());
  }

  private boolean containsOngoingGames(List<GameData> games) {
    return games.stream().anyMatch(game -> !game.isFinished());
  }

  private List<GameData> reverseListOfGames(List<GameData> games) {
    List<GameData> result = new ArrayList<>();
    for (int i = games.size() - 1; i >= 0; i--) {
      result.add(games.get(i));
    }
    return result;
  }
}
