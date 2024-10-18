package com.proxiad.trainee.interfaces;

import java.util.List;
import com.proxiad.trainee.GameData;
import com.proxiad.trainee.exceptions.GameNotFoundException;
import jakarta.servlet.http.HttpSession;

public interface GameRepository {
  GameData getGame(Integer id, HttpSession session) throws GameNotFoundException;

  void saveGame(GameData game, HttpSession session);

  void updateGame(GameData updateData, HttpSession session) throws GameNotFoundException;

  List<GameData> getAllGames(HttpSession session);

  List<GameData> getAllFinishedGames(HttpSession session);

  List<GameData> getAllOngoingGames(HttpSession session);
}
