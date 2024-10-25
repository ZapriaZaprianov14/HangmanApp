package com.proxiad.trainee.interfaces;

import java.util.List;
import com.proxiad.trainee.GameData;
import com.proxiad.trainee.exceptions.GameNotFoundException;
import jakarta.servlet.http.HttpSession;

public interface GameRepository {
  GameData getGame(Long id, HttpSession session) throws GameNotFoundException;

  void postGame(GameData game, HttpSession session);

  void updateGame(GameData updateData, HttpSession session) throws GameNotFoundException;

  List<GameData> getAllFinishedGames(HttpSession session);

  List<GameData> getAllOngoingGames(HttpSession session);
}
