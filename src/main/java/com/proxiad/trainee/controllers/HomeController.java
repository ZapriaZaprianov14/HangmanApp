package com.proxiad.trainee.controllers;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import static com.proxiad.trainee.Constants.PREVIOUS_GAMES;
import com.proxiad.trainee.GameData;
import com.proxiad.trainee.interfaces.GameRepository;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/api")
public class HomeController {

  @Autowired private GameRepository repository;

  @GetMapping("/home")
  public String getHome() {
    return "home-view";
  }

  @GetMapping("/ongoing")
  public String getOngoing(HttpSession session, Model model) {
    List<GameData> ongoingGames = repository.getAllOngoingGames(session);
    model.addAttribute("gamesReversed", reverseListOfGames(ongoingGames));
    return "ongoing-view";
  }

  @GetMapping("/history")
  public String getFinished(HttpSession session, Model model) {
    List<GameData> finishedGames = repository.getAllFinishedGames(session);
    model.addAttribute("gamesReversed", reverseListOfGames(finishedGames));
    return "history-view";
  }

  @ExceptionHandler(value = Exception.class)
  public String unexpectedException(Exception ex, Model model) {
    model.addAttribute("message", "Unexpected error");
    return "unexpected-view";
  }

  private List<GameData> reverseListOfGames(List<GameData> games) {
    List<GameData> result = new ArrayList<>();
    for (int i = games.size() - 1; i >= 0; i--) {
      result.add(games.get(i));
    }
    return result;
  }
}
