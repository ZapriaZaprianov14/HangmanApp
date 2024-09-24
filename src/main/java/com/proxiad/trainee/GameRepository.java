package com.proxiad.trainee;

import java.util.List;
import java.util.UUID;
import jakarta.servlet.http.HttpSession;

public interface GameRepository {
  GameData getGame(UUID id, HttpSession session);

  void saveGame(GameData game, HttpSession session);

  void setCurrentGame(GameData game, HttpSession session);

  GameData getCurrentGame(HttpSession session);

  List<GameData> getAllGames(HttpSession session);

  void leaveGame(HttpSession session);

  GameData resumeGame(UUID id, HttpSession session);
}
