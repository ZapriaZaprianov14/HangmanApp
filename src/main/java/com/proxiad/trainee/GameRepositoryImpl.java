package com.proxiad.trainee;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import jakarta.servlet.http.HttpSession;

public class GameRepositoryImpl implements GameRepository {

  @Override
  public GameData getGame(UUID id, HttpSession session) {
    List<GameData> games = getAllGames(session);
    for (GameData game : games) {
      if (game.getId().equals(id)) {
        return game;
      }
    }
    return null;
  }

  @Override
  public void saveGame(GameData game, HttpSession session) {
    List<GameData> games = getAllGames(session);
    if (games == null) {
      games = new ArrayList<GameData>();
    }
    games.add(game);
    session.setAttribute("previousGames", games);
  }

  @Override
  public void leaveGame(HttpSession session) {
    GameData currentGame = getCurrentGame(session);
    session.removeAttribute("currentGame");
    saveGame(currentGame, session);
  }

  @Override
  public void setCurrentGame(GameData game, HttpSession session) {
    session.setAttribute("currentGame", game);
  }

  @Override
  public GameData getCurrentGame(HttpSession session) {
    return (GameData) session.getAttribute("currentGame");
  }

  @Override
  public List<GameData> getAllGames(HttpSession session) {
    return (List<GameData>) session.getAttribute("previousGames");
  }

  @Override
  public GameData resumeGame(UUID id, HttpSession session) {
    GameData game = getGame(id, session);
    session.setAttribute("currentGame", game);
    List<GameData> previousGames = getAllGames(session);
    previousGames.remove(game);
    return game;
  }
}
