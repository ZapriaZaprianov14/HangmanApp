package com.proxiad.trainee.interfaces;

import java.util.List;
import java.util.UUID;
import com.proxiad.trainee.GameData;
import com.proxiad.trainee.NewGameDTO;
import com.proxiad.trainee.exceptions.GameNotFoundException;
import com.proxiad.trainee.exceptions.InvalidCategoryException;
import com.proxiad.trainee.exceptions.InvalidGuessException;
import jakarta.servlet.http.HttpSession;

public interface GameService {
  GameData makeTry(GameData game, String guess, HttpSession session)
      throws InvalidGuessException, GameNotFoundException;

  GameData startNewGame(NewGameDTO gameDTO, HttpSession session) throws InvalidCategoryException;

  GameData getGame(Integer id, HttpSession session) throws GameNotFoundException;

  GameData getCurrentGame(HttpSession session) throws GameNotFoundException;

  void leaveGame(HttpSession session) throws GameNotFoundException;

  GameData resumeGame(Integer id, HttpSession session) throws GameNotFoundException;

  List<GameData> getAllGames(HttpSession session);
}
