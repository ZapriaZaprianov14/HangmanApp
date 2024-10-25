package com.proxiad.trainee.interfaces;

import java.util.List;
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

  GameData getGame(Long id, HttpSession session) throws GameNotFoundException;

  List<GameData> getAllFinishedGames(HttpSession session);

  List<GameData> getAllOngoingGames(HttpSession session);
}
