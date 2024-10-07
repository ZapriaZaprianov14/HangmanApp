package com.proxiad.trainee;

import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
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

  @PostMapping("/newGame/{category}")
  public String startNewSingleplayerGame(
      @PathVariable("category") String category, HttpSession session, Model model)
      throws InvalidWordException, InvalidCategoryException {

    NewGameDTO newGameDTO = new NewGameDTO(null, "SINGLEPLAYER", category);
    gameService.startNewGame(newGameDTO, session);
    return "game";
  }

  @GetMapping("/multiplayer")
  public String showForm(Model model) {
    model.addAttribute("newGameDTO", new NewGameDTO());
    return "multiplayer";
  }

  @PostMapping("/newGame/multiplayer")
  public String startNewMultiplayerGame(
      @Valid @ModelAttribute("newGameDTO") NewGameDTO newGameDTO,
      HttpSession session,
      Model model,
      BindingResult bindingResult)
      throws InvalidWordException, InvalidCategoryException {

    if (bindingResult.hasErrors()) {
      return "multiplayer";
    }

    gameService.startNewGame(newGameDTO, session);
    return "game";
  }

  // @PostMapping("/guess/{guess}")
  // when using path variable
  // the controller just creates new game???
  @PostMapping("/guess")
  public String playGuess(HttpSession session, @RequestParam String guess, Model model)
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

  @ExceptionHandler(value = CustomException.class)
  public String InvalidCategory(CustomException exception, Model model) {
    model.addAttribute("message", exception.getMessage());
    return "bad-request";
  }

  @ExceptionHandler(value = Exception.class)
  public String unexpectedException(Exception ex, Model model) {
    model.addAttribute("message", "Unexpected error");
    return "unexpected";
  }
}
