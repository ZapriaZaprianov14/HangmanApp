package com.proxiad.trainee;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import com.proxiad.trainee.exceptions.GameNotFoundException;
import com.proxiad.trainee.interfaces.GameRepository;
import jakarta.servlet.http.HttpSession;

// @Service
public class GameRepositoryImpl implements GameRepository {

  @Override
  public GameData getGame(UUID id, HttpSession session) throws GameNotFoundException {
    List<GameData> games = getAllGames(session);
    for (GameData game : games) {
      if (game.getId().equals(id)) {
        return game;
      }
    }

    throw new GameNotFoundException("This game does not exist.");
  }

  @Override
  public void saveGame(GameData game, HttpSession session) {
    List<GameData> games = getAllGames(session);
    if (games == null) {
      games = new ArrayList<GameData>();
    }
    games.add(game);
    session.setAttribute(Constants.PREVIOUS_GAMES, games);
  }

  @Override
  public void leaveGame(HttpSession session) throws GameNotFoundException {
    GameData currentGame = getCurrentGame(session);
    session.removeAttribute(Constants.CURRENT_GAME);
    saveGame(currentGame, session);
  }

  @Override
  public void setCurrentGame(GameData game, HttpSession session) {
    session.setAttribute(Constants.CURRENT_GAME, game);
  }

  @Override
  public GameData getCurrentGame(HttpSession session) throws GameNotFoundException {
    GameData game = (GameData) session.getAttribute(Constants.CURRENT_GAME);
    if (game == null) {
      throw new GameNotFoundException("Current game is null");
    }
    return game;
  }

  @Override
  public List<GameData> getAllGames(HttpSession session) {
    return (List<GameData>) session.getAttribute(Constants.PREVIOUS_GAMES);
  }

  @Override
  public GameData resumeGame(UUID id, HttpSession session) throws GameNotFoundException {
    GameData game = getGame(id, session);
    setCurrentGame(game, session);
    List<GameData> previousGames = getAllGames(session);
    previousGames.remove(game);
    return game;
  }
}
