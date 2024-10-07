package com.proxiad.trainee.interfaces;

import java.util.List;
import java.util.UUID;
import com.proxiad.trainee.GameData;
import com.proxiad.trainee.exceptions.GameNotFoundException;
import jakarta.servlet.http.HttpSession;

public interface GameRepository {
  GameData getGame(UUID id, HttpSession session) throws GameNotFoundException;

  void saveGame(GameData game, HttpSession session);

  void setCurrentGame(GameData game, HttpSession session);

  GameData getCurrentGame(HttpSession session) throws GameNotFoundException;

  List<GameData> getAllGames(HttpSession session);

  void leaveGame(HttpSession session) throws GameNotFoundException;

  GameData resumeGame(UUID id, HttpSession session) throws GameNotFoundException;
}
