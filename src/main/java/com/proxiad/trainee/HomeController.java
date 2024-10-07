package com.proxiad.trainee;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.HttpSessionRequiredException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
@SessionAttributes("previousGames")
public class HomeController {

  @GetMapping("/home")
  public String getHome() {
    return "home";
  }

  @GetMapping("/ongoing")
  public String getOngoing(@ModelAttribute("previousGames") List<GameData> games, Model model) {
    if (containsOngoingGames(games)) {
      model.addAttribute("gamesReversed", reverseListOfGames(games));
      return "ongoing";
    } else {
      model.addAttribute("message", "No ongoing games.");
      return "empty";
    }
  }

  @GetMapping("/history")
  public String getPrevious(@ModelAttribute("previousGames") List<GameData> games, Model model) {
    if (containsFinishedGames(games)) {
      model.addAttribute("gamesReversed", reverseListOfGames(games));
      return "history";
    } else {
      model.addAttribute("message", "No finished games.");
      return "empty";
    }
  }

  @ExceptionHandler(value = Exception.class)
  public String unexpectedException(Exception ex, Model model) {
    model.addAttribute("message", "Unexpected error");
    return "unexpected";
  }

  public boolean containsFinishedGames(List<GameData> games) {
    return games.stream().anyMatch(game -> game.isFinished());
  }

  public boolean containsOngoingGames(List<GameData> games) {
    return games.stream().anyMatch(game -> !game.isFinished());
  }

  public List<GameData> reverseListOfGames(List<GameData> games) {
    List<GameData> result = new ArrayList<>();
    for (int i = games.size() - 1; i >= 0; i--) {
      result.add(games.get(i));
    }
    return result;
  }
}
