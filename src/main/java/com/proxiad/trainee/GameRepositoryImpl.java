package com.proxiad.trainee;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Repository;
import com.proxiad.trainee.exceptions.GameNotFoundException;
import com.proxiad.trainee.interfaces.GameRepository;
import jakarta.servlet.http.HttpSession;

@Repository
public class GameRepositoryImpl implements GameRepository {

  @Override
  public GameData getGame(Long id, HttpSession session) throws GameNotFoundException {
    List<GameData> games = getAllGames(session);

    for (int i = games.size() - 1; i >= 0; i--) {
      GameData game = games.get(i);
      if (game.getId() == id) {
        return game;
      }
    }

    throw new GameNotFoundException("This game does not exist.");
  }

  @Override
  public void updateGame(GameData updateData, HttpSession session) throws GameNotFoundException {
    List<GameData> games = getAllGames(session);
    for (int i = 0; i < games.size(); i++) {
      if (games.get(i).getId() == updateData.getId()) {
        games.set(i, updateData);
      }
    }
    session.setAttribute(Constants.PREVIOUS_GAMES, games);
  }

  @Override
  public void postGame(GameData game, HttpSession session) {
    List<GameData> games = getAllGames(session);
    if (games == null) {
      games = new ArrayList<GameData>();
    }
    games.add(game);
    session.setAttribute(Constants.PREVIOUS_GAMES, games);
  }

  private List<GameData> getAllGames(HttpSession session) {
    List<GameData> allGames = (List<GameData>) session.getAttribute(Constants.PREVIOUS_GAMES);
    if (allGames == null) {
      return new ArrayList<GameData>();
    }

    return allGames;
  }

  @Override
  public List<GameData> getAllFinishedGames(HttpSession session) {
    List<GameData> allGames = getAllGames(session);
    return allGames.stream().filter(x -> x.isFinished()).toList();
  }

  @Override
  public List<GameData> getAllOngoingGames(HttpSession session) {
    List<GameData> allGames = getAllGames(session);
    return allGames.stream().filter(x -> !x.isFinished()).toList();
  }
}
