package com.proxiad.trainee;

import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.type.filter.AbstractClassTestingTypeFilter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.proxiad.trainee.exceptions.CustomException;
import com.proxiad.trainee.exceptions.GameNotFoundException;
import com.proxiad.trainee.exceptions.InvalidCategoryException;
import com.proxiad.trainee.exceptions.InvalidGuessException;
import com.proxiad.trainee.exceptions.InvalidWordException;
import com.proxiad.trainee.interfaces.GameService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/games")
public class GameController {
  @Autowired private GameService gameService;

  @PostMapping("/game/{category}")
  public String startNewSingleplayerGame(
      @PathVariable("category") String category, HttpSession session, Model model)
      throws InvalidWordException, InvalidCategoryException {
    NewGameDTO newGameDTO = new NewGameDTO(null, "SINGLEPLAYER", category);
    gameService.startNewGame(newGameDTO, session);
    return "game";
  }

  @PostMapping("/game/multiplayer")
  public String startNewMultiplayerGame(
      HttpSession session,
      @Valid @ModelAttribute("newGameDTO") NewGameDTO newGameDTO,
      BindingResult bindingResult,
      Model model)
      throws InvalidWordException, InvalidCategoryException {

    if (bindingResult.hasErrors()) {
      return "multiplayer-input";
    }

    gameService.startNewGame(newGameDTO, session);
    return "game";
  }

  @GetMapping("/multiplayerInput")
  public String showMultiplayerForm(Model model) {
    model.addAttribute("newGameDTO", new NewGameDTO());
    return "multiplayer-input";
  }

  @PostMapping("/guess/{guess}")
  public String playGuess(HttpSession session, @PathVariable String guess, Model model)
      throws InvalidGuessException, GameNotFoundException {
    GameData game = gameService.getCurrentGame(session);

    game = gameService.makeTry(game, guess, session);
    if (game.isFinished()) {
      model.addAttribute("word", game.getWordProgress());
      if (game.isGameWon()) {
        return "win";
      } else {
        return "loss";
      }
    }
    return "game";
  }

  @PostMapping("{gameId}/resume")
  public String resumeGame(HttpSession session, @PathVariable String gameId, Model model)
      throws GameNotFoundException {
    gameService.resumeGame(UUID.fromString(gameId), session);
    return "game";
  }

  @PostMapping("/leave")
  public String leaveGame(HttpSession session) throws GameNotFoundException {
    gameService.leaveGame(session);
    return "home";
  }

  @GetMapping("/leave")
  public String getHomeJSP() {
    return "home";
  }

  @GetMapping({"/game/{category}", "/{gameId}/resume", "/guess/{guess}", "/game/multiplayer"})
  public String handleGameRequests(HttpSession session) throws GameNotFoundException {
    gameService.getCurrentGame(session);
    return "game";
  }

  @ExceptionHandler(value = CustomException.class)
  public String InvalidCategory(CustomException exception, Model model) {
    model.addAttribute("message", exception.getMessage());
    return "bad-request";
  }

  @ExceptionHandler(value = Exception.class)
  public String unexpectedException(Exception ex, Model model) {
    model.addAttribute("message", "Page not found");
    return "unexpected";
  }
}
