package com.proxiad.trainee.controllers;

import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.proxiad.trainee.Constants;
import com.proxiad.trainee.GameData;
import com.proxiad.trainee.NewGameDTO;
import com.proxiad.trainee.exceptions.CustomException;
import com.proxiad.trainee.exceptions.GameNotFoundException;
import com.proxiad.trainee.exceptions.InvalidCategoryException;
import com.proxiad.trainee.exceptions.InvalidGuessException;
import com.proxiad.trainee.interfaces.GameService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/api/games")
public class GameController {
  @Autowired private GameService gameService;

  @PostMapping("singleplayer/category/{category}")
  public String startNewSingleplayerGame(
      @PathVariable("category") String category, HttpSession session, Model model)
      throws InvalidCategoryException {
    NewGameDTO newGameDTO = new NewGameDTO(null, "SINGLEPLAYER", category);
    GameData game = gameService.startNewGame(newGameDTO, session);
    model.addAttribute(Constants.CURRENT_GAME, game);
    return "game-view";
  }

  @PostMapping("/multiplayer")
  public String startNewMultiplayerGame(
      HttpSession session,
      @Validated @ModelAttribute("newGameDTO") NewGameDTO newGameDTO,
      BindingResult bindingResult,
      Model model)
      throws InvalidCategoryException {

    if (bindingResult.hasErrors()) {
      return "multiplayer-input-view";
    }

    GameData game = gameService.startNewGame(newGameDTO, session);
    model.addAttribute(Constants.CURRENT_GAME, game);
    return "game-view";
  }

  @GetMapping("/multiplayerInput")
  public String showMultiplayerForm(Model model) {
    model.addAttribute("newGameDTO", new NewGameDTO());
    return "multiplayer-input-view";
  }

  // @PutMapping("/guess/{guess}")
  @PostMapping("/{gameId}/guess/{guess}")
  public String playGuess(
      HttpSession session, @PathVariable int gameId, @PathVariable String guess, Model model)
      throws InvalidGuessException, GameNotFoundException {
    GameData currentGame = gameService.getGame(gameId, session);

    currentGame = gameService.makeTry(currentGame, guess, session);

    if (currentGame.isFinished()) {
      model.addAttribute("word", currentGame.getWordProgress());
      if (currentGame.isGameWon()) {
        return "win-view";
      } else {
        return "loss-view";
      }
    }
    model.addAttribute(Constants.CURRENT_GAME, currentGame);
    return "game-view";
  }

  @PostMapping("{gameId}/resume")
  public String resumeGame(HttpSession session, @PathVariable int gameId, Model model)
      throws GameNotFoundException {
    GameData currentGame = gameService.getGame(gameId, session);
    model.addAttribute(Constants.CURRENT_GAME, currentGame);
    return "game-view";
  }

  @PostMapping("/leave")
  public String leaveGame(HttpSession session) {
    return "home-view";
  }

  @GetMapping("/leave")
  public String getHomeJSP() {
    return "home-view";
  }

  //  @GetMapping({
  //    "singleplayer/category/{category}",
  //    "/{gameId}/resume",
  //    "/guess/{guess}",
  //    "/multiplayer"
  //  })
  //  public String handleGetRequests(HttpSession session) throws GameNotFoundException {
  //    // if current game is null throws exception
  //    gameService.getCurrentGame(session);
  //    return "bad-request-view";
  //  }

  @ExceptionHandler(value = CustomException.class)
  public String handleCustomException(
      CustomException exception, Model model, HttpServletResponse response) {
    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
    model.addAttribute("message", exception.getMessage());
    return "bad-request-view";
  }
}
