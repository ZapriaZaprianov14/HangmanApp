package com.proxiad.trainee.controllers;

import java.util.ArrayList;
import java.util.List;
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

@Controller
@RequestMapping("/api/v1/games")
public class GameController {
  private final GameService gameService;

  @Autowired
  public GameController(GameService gameService) {
    this.gameService = gameService;
  }

  @PostMapping("/singleplayer/category/{category}")
  public String startNewSingleplayerGame(
      @PathVariable("category") String category, HttpSession session, Model model)
      throws InvalidCategoryException {
    NewGameDTO newGameDTO = new NewGameDTO(null, "SINGLEPLAYER", category);
    GameData game = gameService.startNewGame(newGameDTO, session);
    return "redirect:/api/v1/games/" + game.getId();
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
    return "redirect:/api/v1/games/" + game.getId();
  }

  // @PutMapping("/{gameId}/guess/{guess}")
  @PostMapping("/{gameId}/guess/{guess}")
  public String playGuess(
      HttpSession session, @PathVariable Long gameId, @PathVariable String guess, Model model)
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

  @GetMapping("/ongoing")
  public String getOngoing(HttpSession session, Model model) {
    List<GameData> ongoingGames = gameService.getAllOngoingGames(session);
    model.addAttribute("gamesReversed", reverseListOfGames(ongoingGames));
    return "ongoing-view";
  }

  @GetMapping("/finished")
  public String getFinished(HttpSession session, Model model) {
    List<GameData> finishedGames = gameService.getAllFinishedGames(session);
    model.addAttribute("gamesReversed", reverseListOfGames(finishedGames));
    return "finished-view";
  }

  @GetMapping("/multiplayer")
  public String showMultiplayerForm(Model model) {
    model.addAttribute("newGameDTO", new NewGameDTO());
    return "multiplayer-input-view";
  }

  @GetMapping("/{gameId}")
  public String getGame(HttpSession session, @PathVariable Long gameId, Model model)
      throws GameNotFoundException {
    GameData currentGame = gameService.getGame(gameId, session);
    model.addAttribute(Constants.CURRENT_GAME, currentGame);
    return "game-view";
  }

  @GetMapping("/{gameId}/guess/{guess}")
  public String getGameWithGuess(HttpSession session, @PathVariable Long gameId, Model model) {
    return "redirect:/api/v1/games/" + gameId;
  }

  @GetMapping("/leave")
  public String getHomeJSP() {
    return "home-view";
  }

  @ExceptionHandler(value = CustomException.class)
  public String handleCustomException(
      CustomException exception, Model model, HttpServletResponse response) {
    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
    model.addAttribute("message", exception.getMessage());
    return "bad-request-view";
  }

  private List<GameData> reverseListOfGames(List<GameData> games) {
    List<GameData> result = new ArrayList<>();
    for (int i = games.size() - 1; i >= 0; i--) {
      result.add(games.get(i));
    }
    return result;
  }
}
