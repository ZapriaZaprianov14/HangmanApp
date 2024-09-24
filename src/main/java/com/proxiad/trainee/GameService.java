package com.proxiad.trainee;

import java.util.List;
import java.util.UUID;
import jakarta.servlet.http.HttpSession;

public interface GameService {
  GameData makeTry(GameData game, String guess, HttpSession session);

  GameData startNewGame(String category, String wordToGuess, HttpSession session)
      throws InvalidWordException;

  GameData getGame(UUID id, HttpSession session);

  GameData getCurrentGame(HttpSession session);

  void leaveGame(HttpSession session);

  GameData resumeGame(UUID id, HttpSession session);

  List<GameData> getAllGames(HttpSession session);
}
