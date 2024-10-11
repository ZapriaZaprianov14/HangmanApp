package com.proxiad.trainee.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.HttpSessionRequiredException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.NoHandlerFoundException;
import com.proxiad.trainee.GameData;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/api")
@SessionAttributes("previousGames")
public class HomeController {

  @GetMapping()
  public String getHome() {
    return "home-view";
  }

  @GetMapping("/ongoing")
  public String getOngoing(@ModelAttribute("previousGames") List<GameData> games, Model model) {
    if (containsOngoingGames(games)) {
      model.addAttribute("gamesReversed", reverseListOfGames(games));
      return "ongoing-view";
    } else {
      model.addAttribute("message", "No ongoing games.");
      return "empty-view";
    }
  }

  @GetMapping("/history")
  public String getPrevious(@ModelAttribute("previousGames") List<GameData> games, Model model) {
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

  @ExceptionHandler(NoHandlerFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public String handleNotFound(NoHandlerFoundException ex, Model model) {
    model.addAttribute("message", "Page not found");
    return "unexpected-view";
  }
}
